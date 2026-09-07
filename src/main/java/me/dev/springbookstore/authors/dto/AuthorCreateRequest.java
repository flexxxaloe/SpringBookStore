package me.dev.springbookstore.authors.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record AuthorCreateRequest (

        @NotNull
        String name,

        String bio,

        @NotNull
        @Past
        LocalDate bornDate

) {}

