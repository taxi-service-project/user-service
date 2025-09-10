package com.example.user_service.dto;

public record UserUpdateResponse(
        Long id,
        String name,
        String email,
        String phoneNumber
) {
}
