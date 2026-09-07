package me.dev.springbookstore.books.dto;

import jakarta.validation.constraints.*;

import java.time.LocalDate;
import java.time.Year;

public record BookPatchRequest (


        String title,

        Long authorId,

        String description,

        @PastOrPresent
        LocalDate publicationDate,

        @PastOrPresent
        Year writtenYear,

        @PositiveOrZero
        Integer amount,

        @Positive
        Long price
) {}