package me.dev.springbookstore.users.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record LoginRequest (

        @NotBlank
        @Length(min = 3, max = 25)
        String username,

        @NotBlank
        @Length(min = 8, max = 60)
        String password
) {
}
