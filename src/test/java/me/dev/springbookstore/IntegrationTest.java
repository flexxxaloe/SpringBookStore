package me.dev.springbookstore;


import jakarta.persistence.EntityManager;
import me.dev.springbookstore.authors.AuthorEntity;
import me.dev.springbookstore.authors.AuthorRepository;
import me.dev.springbookstore.books.BookEntity;
import me.dev.springbookstore.books.BookStoreRepository;
import me.dev.springbookstore.orders.OrderRepository;
import me.dev.springbookstore.orders.OrderService;
import me.dev.springbookstore.orders.dto.OrderCreateRequest;
import me.dev.springbookstore.orders.dto.OrderItemCreateRequest;
import me.dev.springbookstore.users.UserEntity;
import me.dev.springbookstore.users.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.concurrent.DelegatingSecurityContextExecutorService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.doAnswer;


@SpringBootTest(properties = {
        "jwt.secret=7d3f8a1c6b9e4f2a0d5c8e1b7a4f9d2c6e8a3",
        "app.admin.username=testadmin",
        "app.admin.password=testpassword",
        "app.admin.email=test@example.com"
})
@Testcontainers
public class IntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:16");

    // Keep the real bean, but allow this test to intercept findById().
    @MockitoSpyBean
    private BookStoreRepository bookRepository;

    // Spring proxy: each worker uses the EntityManager of its own transaction.
    @Autowired
    private EntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private AuthorRepository authorRepository;


    @BeforeEach
    void setUp() {
        cleanDatabase();
        setUpCurrentUser();
    }


    void cleanDatabase() {
        orderRepository.deleteAll();
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        userRepository.deleteAll();
    }

    void setUpCurrentUser() {
        UserEntity user = new UserEntity();
        user.setUsername("user");
        user.setPassword("password");
        user.setEmail("user@email.com");
        userRepository.saveAndFlush(user);

        UserDetails userDetails = User
                .withUsername("user")
                .password("password")
                .roles("USER")
                .build();

        var authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
    }

    @AfterEach
    void clearSecurity() {
        SecurityContextHolder.clearContext();
    }


    @Test
    void shouldCreateOrderAndDecreaseBookAmount() {
        AuthorEntity author = new AuthorEntity();
        author.setName("Test Author");
        author.setBornDate(LocalDate.now());
        authorRepository.saveAndFlush(author);

        BookEntity availableBook = new BookEntity();
        availableBook.setTitle("Book 1");
        availableBook.setAmount(2);
        availableBook.setPublicationDate(LocalDate.now());
        availableBook.setPrice(1000L);
        availableBook.setAuthor(author);
        bookRepository.saveAndFlush(availableBook);



        OrderItemCreateRequest first =
                new OrderItemCreateRequest(availableBook.getId(), 1);



        orderService.createOrder(new OrderCreateRequest(List.of(first)));


        BookEntity book = bookRepository.findById(availableBook.getId()).orElseThrow();

        assertThat(book.getAmount()).isEqualTo(1);
        assertThat(orderRepository.count()).isEqualTo(1);

    }

    @Test
    void shouldRollbackOrderWhenOneBookIsOutOfStock() {
        AuthorEntity author = new AuthorEntity();
        author.setName("Test Author");
        author.setBornDate(LocalDate.now());
        authorRepository.saveAndFlush(author);

        BookEntity availableBook = new BookEntity();
        availableBook.setTitle("Book 1");
        availableBook.setAmount(2);
        availableBook.setPublicationDate(LocalDate.now());
        availableBook.setPrice(1000L);
        availableBook.setAuthor(author);
        bookRepository.saveAndFlush(availableBook);

        BookEntity outOfStockBook = new BookEntity();
        outOfStockBook.setTitle("Book 2");
        outOfStockBook.setAmount(0);
        outOfStockBook.setPublicationDate(LocalDate.now());
        outOfStockBook.setPrice(2000L);
        outOfStockBook.setAuthor(author);
        bookRepository.saveAndFlush(outOfStockBook);


        OrderItemCreateRequest first =
                new OrderItemCreateRequest(availableBook.getId(), 1);

        OrderItemCreateRequest second =
                new OrderItemCreateRequest(outOfStockBook.getId(), 1);


        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderService.createOrder(new OrderCreateRequest(List.of(first, second))))
                .withMessage("Not enough stock for book: " + outOfStockBook.getTitle());


        BookEntity firstAfter = bookRepository.findById(availableBook.getId()).orElseThrow();
        BookEntity secondAfter = bookRepository.findById(outOfStockBook.getId()).orElseThrow();

        assertThat(firstAfter.getAmount()).isEqualTo(2);
        assertThat(secondAfter.getAmount()).isZero();
        assertThat(orderRepository.count()).isZero();

    }



}

