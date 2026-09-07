package me.dev.springbookstore.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record UserCreate (

        @NotBlank
        @Length(min = 3, max = 25)
        String username,

        @NotBlank
        @Email
        String email,

        @NotBlank
        @Length(min = 8, max = 60)
        String password

) {
}
