package me.dev.springbookstore.books.config;


import me.dev.springbookstore.users.UserEntity;
import me.dev.springbookstore.users.UserRepository;
import me.dev.springbookstore.users.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class MainConfig {

    @Bean
    public ApplicationRunner createInitialAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${app.admin.username}") String username,
            @Value("${app.admin.password}") String password,
            @Value("${app.admin.email}") String email
                           ) {

        return args -> {
            if (!userRepository.existsByUsername(username)) {
                UserEntity user = new UserEntity();
                user.setUsername(username);
                user.setEmail(email);
                user.setPassword(passwordEncoder.encode(password));

                user.setRole(UserRole.ADMIN);

                userRepository.save(user);
            }
        };

    }
}
