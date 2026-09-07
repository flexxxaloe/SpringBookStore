package me.dev.springbookstore.authors.dto;

import java.time.LocalDate;

public record AuthorResponse(

        Long id,

        String name,

        String bio,

        LocalDate bornDate
) {
}
