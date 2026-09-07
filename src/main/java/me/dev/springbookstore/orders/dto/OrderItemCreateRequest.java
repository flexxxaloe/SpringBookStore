package me.dev.springbookstore.orders.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderItemCreateRequest(
        @NotNull
        Long bookId,

        @NotNull
        @Positive
        Integer quantity
) {
}
