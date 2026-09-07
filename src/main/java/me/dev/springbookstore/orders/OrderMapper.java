package me.dev.springbookstore.orders;

import me.dev.springbookstore.orders.dto.OrderItemResponse;
import me.dev.springbookstore.orders.dto.OrderResponse;
import me.dev.springbookstore.orders.entity.OrderEntity;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderResponse toResponse(OrderEntity order) {
        var items = order.getItems().stream()
                .map(item -> new OrderItemResponse(
                        item.getBook().getId(),
                        item.getBook().getTitle(),
                        item.getQuantity(),
                        item.getPriceAtPurchase(),
                        item.getPriceAtPurchase() * item.getQuantity()
                ))
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getTotalPrice(),
                order.getCreatedAt(),
                items
        );
    }
}
