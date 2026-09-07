package me.dev.springbookstore.orders;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.dev.springbookstore.orders.dto.OrderCreateRequest;
import me.dev.springbookstore.orders.dto.OrderResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderCreateRequest request) {
        log.info("Creating order with {} items", request.items().size());
        OrderResponse response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<Page<OrderResponse>> getMyOrders(
            @PageableDefault(size = 20) Pageable pageable) {
        log.info("Fetching orders for current user");
        return ResponseEntity.ok(orderService.getMyOrders(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long id) {
        log.info("Fetching order {}", id);
        return ResponseEntity.ok(orderService.getOrder(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/{id}")
    public ResponseEntity<OrderResponse> getAnyOrder(@PathVariable Long id) {
        log.info("Fetching order with role admin {}", id);
        return ResponseEntity.ok(orderService.getAnyOrder(id));
    }
}
