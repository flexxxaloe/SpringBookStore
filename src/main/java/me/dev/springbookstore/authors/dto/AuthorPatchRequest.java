package me.dev.springbookstore.authors.dto;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record AuthorPatchRequest(

        @Size(max = 255)
        String name,

        @Size(max = 500)
        String bio,

        @Past
        LocalDate bornDate
) {

}
