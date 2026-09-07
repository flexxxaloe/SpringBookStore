package me.dev.springbookstore.auth;


import jakarta.validation.Valid;
import me.dev.springbookstore.users.dto.JwtResponse;
import me.dev.springbookstore.users.dto.LoginRequest;
import me.dev.springbookstore.users.dto.RegisterRequest;
import me.dev.springbookstore.users.dto.UserResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    private final Logger log =  LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest register) {
        log.info("Registration attempt for user: {}", register.username());
        UserResponse response = authService.register(register);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest login) {
        log.info("Login attempt for user: {}", login.username());
        JwtResponse response = authService.login(login);
        return ResponseEntity.ok(response);
    }
}
