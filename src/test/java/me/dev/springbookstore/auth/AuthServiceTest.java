package me.dev.springbookstore.auth;


import me.dev.springbookstore.books.config.MainConfig;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    JwtService jwtService;
    @InjectMocks
    AuthService authService;

    private MainConfig config;

    @BeforeEach
    void setUp() {
        config = new MainConfig();
    }


    @Test
    void shouldCreateAdminWithAdminRoleWhenUserDoesNotExist() throws Exception {
        String username = "admin";
        String password = "admin2224";
        String email = "admin@email.com";

        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(passwordEncoder.encode(password))
                .thenReturn("encodedPassword");

        ApplicationRunner applicationRunner = config.createInitialAdmin(
                userRepository,
                passwordEncoder,
                username,
                password,
                email
        );

        applicationRunner.run(null);



        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userRepository).save(captor.capture());
        UserEntity savedUser = captor.getValue();



        assertEquals(username, savedUser.getUsername());
        assertEquals(email, savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertEquals(UserRole.ADMIN, savedUser.getRole());


    }

    @Test
    void shouldNotCreateAdminWhenUserAlreadyExists() throws Exception {


        String username = "admin";
        String password = "admin2224";
        String email = "admin@email.com";

        when(userRepository.existsByUsername(username)).thenReturn(true);

        ApplicationRunner applicationRunner = config.createInitialAdmin(
                userRepository,
                passwordEncoder,
                username,
                password,
                email
        );

        applicationRunner.run(null);
        verify(userRepository, never()).save(any(UserEntity.class));

    }

    @Test
    void shouldRegisterUserWhenUsernameAndEmailAreAvailable() {

        String username = "user";
        String password = "user2224";
        String email = "user@email.com";
        String encodedPassword = "encodedPassword";

        RegisterRequest request = new RegisterRequest(username, email, password);

        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);

        UserEntity savedEntity = new UserEntity();
        savedEntity.setId(1L);

        when(userRepository.save(any(UserEntity.class))).thenReturn(savedEntity);


        authService.register(request);

        ArgumentCaptor<UserEntity> captor = ArgumentCaptor.forClass(UserEntity.class);

        verify(userRepository).save(captor.capture());

        UserEntity savedUser = captor.getValue();


        assertEquals(username, savedUser.getUsername());
        assertEquals(email, savedUser.getEmail());
        assertEquals(encodedPassword, savedUser.getPassword());
        assertEquals(UserRole.USER, savedUser.getRole());


    }

    @Test
    void shouldThrowExceptionWhenUsernameAlreadyExists() {

        String username = "username";
        String email = "email";
        String password = "password";
        when(userRepository.existsByUsername(username)).thenReturn(true);

        RegisterRequest request = new RegisterRequest(
                username,
                email,
                password
        );


        assertThatExceptionOfType(UsernameAlreadyExistsException.class)
                .isThrownBy(() -> authService.register(request));


        verify(userRepository, never()).save(any(UserEntity.class));

    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        String username = "username";
        String email = "email";
        String password = "password";
        when(userRepository.existsByEmail(email)).thenReturn(true);

        RegisterRequest request = new RegisterRequest(
                username,
                email,
                password
        );

        assertThatExceptionOfType(EmailAlreadyExistsException.class)
                .isThrownBy(() -> authService.register(request));


        verify(userRepository, never()).save(any(UserEntity.class));


    }




    @Test
    void shouldLoginSuccessfullyWithValidCredentials() {
        String username = "username";
        String password = "password";
        String token = "jwt-token";
        LoginRequest request = new LoginRequest(username, password);


        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(authentication);

        when(jwtService.generateToken(username))
                .thenReturn(token);

        JwtResponse response = authService.login(request);

        assertEquals(token, response.token());

    }

    @Test
    void shouldThrowExceptionWhenCredentialsAreInvalid() {

        String username = "username";
        String password = "password";


        LoginRequest request = new LoginRequest(username, password);

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThatExceptionOfType(BadCredentialsException.class).isThrownBy(() -> authService.login(request));
        verify(jwtService, never()).generateToken(username);

    }

}
