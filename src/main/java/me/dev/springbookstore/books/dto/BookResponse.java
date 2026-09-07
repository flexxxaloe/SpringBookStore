package me.dev.springbookstore.books.dto;

import me.dev.springbookstore.books.BookStatus;

import java.time.LocalDate;
import java.time.Year;

public record BookResponse(
        Long id,
        String title,
        Long authorId,
        String authorName,
        String description,
        LocalDate publicationDate,
        Year writtenYear,
        Integer amount,
        Long price,
        BookStatus bookStatus
) {}
