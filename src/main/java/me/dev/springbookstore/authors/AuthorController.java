package me.dev.springbookstore.authors;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.dev.springbookstore.authors.dto.AuthorCreateRequest;
import me.dev.springbookstore.authors.dto.AuthorResponse;
import me.dev.springbookstore.books.BookStoreService;
import me.dev.springbookstore.books.dto.BookResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController {

    private static final Logger log = LoggerFactory.getLogger(AuthorController.class);

    private final AuthorService authorService;



    @GetMapping("/{id}/books")
    public ResponseEntity<Page<BookResponse>> getBooksOfAuthor(
            @PathVariable("id") Long authorId,
            Pageable pageable) {

        log.info(
                "Calling getBooksOfAuthor with author {} and page {}, size {}, offset {}",
                authorId,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getOffset()
        );
        //GET /authors/5/books?page=0&size=20
        return ResponseEntity.ok(authorService.getBooksOfAuthor(authorId, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponse> getAuthor(@PathVariable("id") Long authorId) {

        return ResponseEntity.ok(authorService.getAuthor(authorId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<AuthorResponse> addAuthor(@Valid @RequestBody AuthorCreateRequest createAuthor) {
        log.info("Calling addAuthor with author {}", createAuthor);

        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.addAuthor(createAuthor));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<AuthorResponse> deleteAuthor(@PathVariable("id") Long authorId) {
        log.info("Calling deleteAuthor with id {}", authorId);

        authorService.deleteAuthor(authorId);
        return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
