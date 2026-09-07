package me.dev.springbookstore.orders.dto;

import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        Long totalPrice,
        LocalDateTime createdAt,
        List<OrderItemResponse> items
) {
}
