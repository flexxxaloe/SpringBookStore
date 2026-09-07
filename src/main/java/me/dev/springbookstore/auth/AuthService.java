package me.dev.springbookstore.auth;


import me.dev.springbookstore.security.JwtService;
import me.dev.springbookstore.users.UserEntity;
import me.dev.springbookstore.users.UserRepository;
import me.dev.springbookstore.users.UserRole;
import me.dev.springbookstore.users.dto.JwtResponse;
import me.dev.springbookstore.users.dto.LoginRequest;
import me.dev.springbookstore.users.dto.RegisterRequest;
import me.dev.springbookstore.users.dto.UserResponse;
import me.dev.springbookstore.web.exceptions.EmailAlreadyExistsException;
import me.dev.springbookstore.web.exceptions.UsernameAlreadyExistsException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public UserResponse register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.username())) {
            throw new UsernameAlreadyExistsException("Username is already in use");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Email is already in use");
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setUsername(request.username());
        userEntity.setEmail(request.email());
        userEntity.setPassword(passwordEncoder.encode(request.password()));

        UserEntity saved = userRepository.save(userEntity);

        return new UserResponse(
                saved.getId(),
                saved.getUsername(),
                saved.getEmail(),
                saved.getRole()
        );
    }

    public JwtResponse login(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.username(),
                        loginRequest.password()
                )
        ); /*
         * UsernamePasswordAuthenticationToken(username, password)
         * represents an unauthenticated login attempt.
         *
         * AuthenticationManager verifies the credentials.
         * If they are valid, it returns an authenticated Authentication object.
         * If they are invalid, an AuthenticationException is thrown.
         *
         * The returned Authentication usually contains:
         * - principal: authenticated user (usually UserDetails)
         * - authorities: user's roles/permissions
         * - credentials: usually null after successful authentication
         */


        // @AuthenticationPrincipal is typically used in controller methods
        // to inject the currently authenticated user from the SecurityContext.
        String token = jwtService.generateToken(loginRequest.username());
        return new JwtResponse(token);
    }

}
