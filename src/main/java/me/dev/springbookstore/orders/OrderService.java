package me.dev.springbookstore.orders;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import me.dev.springbookstore.books.BookEntity;
import me.dev.springbookstore.books.BookStatus;
import me.dev.springbookstore.books.BookStoreRepository;
import me.dev.springbookstore.orders.dto.OrderCreateRequest;
import me.dev.springbookstore.orders.dto.OrderItemCreateRequest;
import me.dev.springbookstore.orders.dto.OrderResponse;
import me.dev.springbookstore.orders.entity.OrderEntity;
import me.dev.springbookstore.orders.entity.OrderItemEntity;
import me.dev.springbookstore.users.UserEntity;
import me.dev.springbookstore.users.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final BookStoreRepository bookStoreRepository;
    private final UserRepository userRepository;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderResponse createOrder(OrderCreateRequest request) {
        UserEntity user = getCurrentUser();
        validateUniqueBookIds(request.items());

        OrderEntity order = new OrderEntity();
        order.setUser(user);

        long totalPrice = 0L;

        for (OrderItemCreateRequest itemRequest : request.items()) {
            BookEntity book = bookStoreRepository.findById(itemRequest.bookId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Book with id " + itemRequest.bookId() + " not found"));

            validateStock(book, itemRequest.quantity());

            book.setAmount(book.getAmount() - itemRequest.quantity());

            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setOrder(order);
            orderItem.setBook(book);
            orderItem.setQuantity(itemRequest.quantity());
            orderItem.setPriceAtPurchase(book.getPrice());

            order.getItems().add(orderItem);
            totalPrice += book.getPrice() * itemRequest.quantity();
        }

        order.setTotalPrice(totalPrice);
        OrderEntity saved = orderRepository.save(order);
        return orderMapper.toResponse(saved);
    }

    public Page<OrderResponse> getMyOrders(Pageable pageable) {
        UserEntity user = getCurrentUser();

        return orderRepository.findByUser_IdOrderByCreatedAtDesc(user.getId(), pageable)
                .map(orderMapper::toResponse);
    }

    public OrderResponse getOrder(Long orderId) {
        UserEntity user = getCurrentUser();

        OrderEntity order = orderRepository.findByIdAndUser_Id(orderId, user.getId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Order with id " + orderId + " not found"));


        return orderMapper.toResponse(order);
    }

    public OrderResponse getAnyOrder(Long orderId) {

        OrderEntity order = orderRepository.findByIdWithItems(orderId)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Order with id " + orderId + " not found"));


        return orderMapper.toResponse(order);
    }

    private void validateUniqueBookIds(List<OrderItemCreateRequest> items) {
        Set<Long> bookIds = new HashSet<>();
        for (OrderItemCreateRequest item : items) {
            if (!bookIds.add(item.bookId())) {
                throw new IllegalArgumentException(
                        "Duplicate book id in order: " + item.bookId());
            }
        }
    }

    private void validateStock(BookEntity book, int quantity) {
        if (book.getBookStatus() == BookStatus.OUT_OF_STOCK || book.getAmount() < quantity) {
            throw new IllegalArgumentException(
                    "Not enough stock for book: " + book.getTitle());
        }
    }

    private UserEntity getCurrentUser() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
