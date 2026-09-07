package me.dev.springbookstore.users.dto;

import me.dev.springbookstore.users.UserRole;

public record UserResponse (
    Long id,
    String username,
    String email,
    UserRole role
) {
}
