package me.dev.springbookstore.authors;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface AuthorRepository extends JpaRepository<AuthorEntity, Long> {
    boolean existsByNameAndBornDate(String name, LocalDate bornDate);
    boolean existsByNameAndBornDateAndIdNot(
            String name,
            LocalDate bornDate,
            Long id
    );
}
