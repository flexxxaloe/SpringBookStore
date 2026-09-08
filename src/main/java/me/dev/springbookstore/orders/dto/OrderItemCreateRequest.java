package me.dev.springbookstore.orders.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderItemCreateRequest(
        @NotNull
        Long bookId,

        @NotNull
        @Min(1)
        @Max(10000)
        Integer quantity
) {
}
