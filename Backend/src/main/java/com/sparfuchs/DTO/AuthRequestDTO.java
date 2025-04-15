package com.sparfuchs.DTO;

public record AuthRequestDTO(
        String username,
        String email,
        String password) {
}

