package me.dev.springbookstore.authors.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AuthorCreateRequest (

        @NotBlank
        @Size(max = 255)
        String name,

        @Size(max = 500)
        String bio,

        @NotNull
        @Past
        LocalDate bornDate

) {}

