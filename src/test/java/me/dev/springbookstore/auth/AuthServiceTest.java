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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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



}
