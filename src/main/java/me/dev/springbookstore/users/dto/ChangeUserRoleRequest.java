package me.dev.springbookstore.users.dto;

import jakarta.validation.constraints.NotNull;
import me.dev.springbookstore.users.UserRole;

public record ChangeUserRoleRequest(
        @NotNull
        UserRole role
) {
}
