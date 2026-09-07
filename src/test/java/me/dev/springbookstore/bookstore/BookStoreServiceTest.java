package me.dev.springbookstore.bookstore;


import me.dev.springbookstore.authors.AuthorRepository;
import me.dev.springbookstore.books.BookEntity;
import me.dev.springbookstore.books.BookMapper;
import me.dev.springbookstore.books.BookStoreRepository;
import me.dev.springbookstore.books.BookStoreService;
import me.dev.springbookstore.books.dto.BookResponse;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

}
