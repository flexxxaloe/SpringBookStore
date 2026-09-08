package me.dev.springbookstore.books.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.Year;


public record BookCreateRequest (


        @NotBlank
        @Size(max = 255)
        String title,

        @NotNull
        Long authorId,

        @Size(max = 255) // change later to 500
        String description,

        @NotNull
        @PastOrPresent
        LocalDate publicationDate,

        @PastOrPresent
        Year writtenYear,

        @NotNull
        @Min(0)
        @Max(1_000_000)
        Integer amount,

        @NotNull
        @Min(1)
        @Max(100_000_000) //price in cents
        Long price
) {}
