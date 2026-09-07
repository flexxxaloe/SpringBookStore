package me.dev.springbookstore.bookstore;


import jakarta.persistence.EntityNotFoundException;
import me.dev.springbookstore.authors.AuthorRepository;
import me.dev.springbookstore.books.BookEntity;
import me.dev.springbookstore.books.BookMapper;
import me.dev.springbookstore.books.BookStoreRepository;
import me.dev.springbookstore.books.BookStoreService;
import me.dev.springbookstore.books.dto.BookResponse;
import me.dev.springbookstore.web.exceptions.UsernameAlreadyExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookStoreServiceTest {

    @Mock
    BookStoreRepository bookStoreRepository;

    @Mock
    BookMapper mapper;

    @Mock
    AuthorRepository authorRepository;

    @InjectMocks
    BookStoreService bookStoreService;



    @Test
    public void testGetBookById() {
        Long id = 1L;

        BookEntity book = new BookEntity();
        book.setId(id);

        BookResponse response = mock(BookResponse.class);

        when(bookStoreRepository.findById(id)).thenReturn(Optional.of(book));
        when(mapper.bookEntityToBookResponse(book)).thenReturn(response);

        BookResponse result = bookStoreService.getBookById(id);

        assertThat(result).isEqualTo(response);

        verify(bookStoreRepository).findById(id);
        verify(mapper).bookEntityToBookResponse(book);
    }

    @Test
    void shouldThrowExceptionWhenBookNotFound() {
        Long bookId = 1L;
        when(bookStoreRepository.findById(bookId)).thenReturn(Optional.empty());
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> bookStoreService.getBookById(bookId));
    }

    @Test
    public void searchBooksWithLessThanThreeSymbols() {
        Pageable pageable = PageRequest.of(0, 10);

        assertThatThrownBy(() ->
                bookStoreService.searchBooks(
                        "ab",
                        null,
                        null,
                        pageable
                ))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Title search requires at least 3 characters");

    }

    @Test
    public void searchBooksWithPageSizeGreaterThanOneHundred() {
        Pageable pageable = PageRequest.of(0, 1000);

        Page<BookEntity> page = new PageImpl<>(
                List.of(),
                PageRequest.of(0, 100),
                100
        );

        when(bookStoreRepository.searchBooks(
                "abc%",
                null,
                null,
                PageRequest.of(0, 100)
        )).thenReturn(page);

        Page<BookResponse> result = bookStoreService.searchBooks(
                "abc",
                null,
                null,
                pageable
        );

        assertThat(result.getSize()).isEqualTo(100);
        verify(bookStoreRepository).searchBooks(
                "abc%",
                null,
                null,
                PageRequest.of(0, 100)
        );

    }
}
