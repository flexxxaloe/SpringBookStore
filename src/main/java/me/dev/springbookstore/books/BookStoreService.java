package me.dev.springbookstore.books;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import me.dev.springbookstore.authors.AuthorEntity;
import me.dev.springbookstore.authors.AuthorRepository;
import me.dev.springbookstore.books.dto.BookCreateRequest;
import me.dev.springbookstore.books.dto.BookPatchRequest;
import me.dev.springbookstore.books.dto.BookResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class BookStoreService {

    private static final int MAX_PAGE_SIZE = 100;

    private final BookStoreRepository bookStoreRepository;
    private final BookMapper mapper;
    private final AuthorRepository authorRepository;


    public BookResponse getBookById(Long id) {
        BookEntity book = bookStoreRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Book with id " + id + " not found"));
        return mapper.bookEntityToBookResponse(book);
    }

    public Page<BookResponse> searchBooks(String title, Long authorId,
                                          BookStatus status, Pageable pageable) {

        if (pageable.getPageSize() > MAX_PAGE_SIZE) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    MAX_PAGE_SIZE,
                    pageable.getSort()
            );
        }

        String searchTitle = normalizeTitle(title);

        if (searchTitle == null && authorId == null && status == null) {
            return bookStoreRepository.findAllWithAuthor(pageable)
                    .map(mapper::bookEntityToBookResponse);
        }

        String titlePattern = searchTitle != null ? searchTitle.toLowerCase() + "%" : null;

        return bookStoreRepository.searchBooks(titlePattern, authorId, status, pageable)
                .map(mapper::bookEntityToBookResponse);
    }


    private String normalizeTitle(String title) {
        if (title == null || title.isBlank()) {
            return null;
        }
        String trimmed = title.trim();
        if (trimmed.length() < 3) {
            throw new IllegalArgumentException(
                    "Title search requires at least 3 characters");
        }
        return trimmed;
    }


    @Transactional
    public BookResponse addBook(BookCreateRequest request) {
        // как проверять уникальность книги? если тайтл повторяться может
        AuthorEntity author = authorRepository.findById(request.authorId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Author not found")
                );

        BookEntity entity = mapper.bookRequestToBookEntity(request, author);

        var savedEntity = bookStoreRepository.save(entity);

        return mapper.bookEntityToBookResponse(savedEntity);
    }

    @Transactional
    public BookResponse patchBook(Long bookId, BookPatchRequest request) {

        if (request.title() != null && request.title().isBlank()) {
            throw new IllegalArgumentException("Title must not be blank");
        }

        var entityBook = bookStoreRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + bookId + " not found"));


        mapper.patchBookEntity(entityBook, request);

        // var saved = bookStoreRepository.save(entityBook); entity стала managed → mapper изменил её поля → при завершении @Transactional Hibernate сам выполнит UPDATE.

        return mapper.bookEntityToBookResponse(entityBook);
    }

    @Transactional
    public BookResponse updateBook(Long bookId, BookCreateRequest request) {


        var entityBook = bookStoreRepository.findById(bookId)
                .orElseThrow(() -> new EntityNotFoundException("Book with id " + bookId + " not found"));

        AuthorEntity author = authorRepository.findById(request.authorId())
                .orElseThrow(() ->
                        new EntityNotFoundException("Author with id " + request.authorId() + " not found")
                );

        entityBook.setAuthor(author);

        BookEntity entity = mapper.updateBookEntity(entityBook, request);

        // var savedEntity = bookStoreRepository.save(entity);

        return mapper.bookEntityToBookResponse(entity);
    }

    @Transactional
    public void deleteBook(Long bookId) {
        var book = bookStoreRepository.findById(bookId).orElseThrow(
                () -> new EntityNotFoundException("Book with id " + bookId + " not found"));

        bookStoreRepository.delete(book);
    }
}
