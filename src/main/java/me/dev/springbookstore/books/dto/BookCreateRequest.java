package me.dev.springbookstore.books.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.Year;


public record BookCreateRequest (


        @NotBlank
        String title,

        @NotNull
        Long authorId,

        String description,

        @NotNull
        @PastOrPresent
        LocalDate publicationDate,

        @PastOrPresent
        Year writtenYear,

        @NotNull
        @PositiveOrZero
        Integer amount,

        @NotNull
        @Positive
        Long price
) {}
