package me.dev.springbookstore.books.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.Year;

public record BookPatchRequest (

        @Size(max = 255)
        String title,

        Long authorId,

        @Size(max = 255)
        String description,

        @PastOrPresent
        LocalDate publicationDate,

        @PastOrPresent
        Year writtenYear,

        @Min(0)
        @Max(1_000_000)
        Integer amount,

        @Positive
        @Max(100_000_000)
        Long price
) {}