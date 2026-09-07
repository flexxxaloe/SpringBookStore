package me.dev.springbookstore.orders.dto;

public record OrderItemResponse(
        Long bookId,
        String bookTitle,
        Integer quantity,
        Long priceAtPurchase,
        Long lineTotal // это че
) {
}
