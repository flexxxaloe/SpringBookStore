package me.dev.springbookstore.books;


import jakarta.validation.Valid;
import me.dev.springbookstore.books.dto.BookCreateRequest;
import me.dev.springbookstore.books.dto.BookPatchRequest;
import me.dev.springbookstore.books.dto.BookResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;


@RestController
@RequestMapping("/catalog")
public class BookStoreController {

    private static final Logger log = LoggerFactory.getLogger(BookStoreController.class);

    private final BookStoreService bookStoreService;

    public BookStoreController(BookStoreService bookStoreService) {
        this.bookStoreService = bookStoreService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable long id) {
        log.info("Calling getBookById with id {}", id);
        return ResponseEntity.status(HttpStatus.OK).body(bookStoreService.getBookById(id));
    }

    @GetMapping
    public String home() {
        return "Book Store API. See /swagger-ui/index.html for documentation.";
    }


    @GetMapping("/search")
    public ResponseEntity<Page<BookResponse>> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Long authorId,
            @RequestParam(required = false) BookStatus status,
            @PageableDefault(size = 20, sort = "title", direction = Sort.Direction.ASC) Pageable pageable) {
        log.info(
                "Calling searchBooks with page {}, size {}, offset {}, sort by {}",
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getOffset(),
                pageable.getSort()
        );

        return ResponseEntity.ok(bookStoreService.searchBooks(title, authorId, status, pageable));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<BookResponse> addBook(@Valid @RequestBody BookCreateRequest createBook) {
        log.info("Calling addBook with book {}", createBook);
        //ResponseEntity.of Он используется, когда ты ищешь объект и не уверен, что он существует.
        /*
        Optional<Book> book = bookService.findById(id);
        return ResponseEntity.of(book);
         */
        return ResponseEntity.status(HttpStatus.CREATED).body(bookStoreService.addBook(createBook));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{bookId}")
    public ResponseEntity<BookResponse> updateBook(@PathVariable long bookId, @Valid @RequestBody BookCreateRequest bookUpdate) {
        log.info("Calling updateBook with id {}", bookId);
        var updated = bookStoreService.updateBook(bookId, bookUpdate);
        return ResponseEntity.ok(updated);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{bookId}")
    public ResponseEntity<BookResponse> patchBook(@PathVariable long bookId, @Valid @RequestBody BookPatchRequest bookUpdate) {
        log.info("Calling patchBook with id {}", bookId);
        var updated = bookStoreService.patchBook(bookId, bookUpdate);
        return ResponseEntity.ok(updated);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable long bookId) {
        log.info("Calling deleteBook with id {}", bookId);
        bookStoreService.deleteBook(bookId);
        return  new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
