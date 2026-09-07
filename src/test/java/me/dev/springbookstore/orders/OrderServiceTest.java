package me.dev.springbookstore.orders;


import me.dev.springbookstore.books.BookEntity;
import me.dev.springbookstore.books.BookStatus;
import me.dev.springbookstore.books.BookStoreRepository;
import me.dev.springbookstore.orders.dto.OrderCreateRequest;
import me.dev.springbookstore.orders.dto.OrderItemCreateRequest;
import me.dev.springbookstore.orders.dto.OrderResponse;
import me.dev.springbookstore.orders.entity.OrderEntity;
import me.dev.springbookstore.users.UserEntity;
import me.dev.springbookstore.users.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;
    @Mock
    BookStoreRepository bookStoreRepository;
    @Mock
    UserRepository userRepository;
    @Spy
    OrderMapper orderMapper = new OrderMapper();
    @InjectMocks
    OrderService orderService;


}
