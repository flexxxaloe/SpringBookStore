package me.dev.springbookstore.authors.dto;

import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record AuthorPatchRequest(

        String name,

        String bio,

        @Past
        LocalDate bornDate
) {

}
