package me.dev.springbookstore.orders.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderCreateRequest(
        @NotEmpty
        @Valid
        List<@NotNull OrderItemCreateRequest> items
) {
}
