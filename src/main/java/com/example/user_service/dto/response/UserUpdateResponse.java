package com.example.user_service.dto.response;

public record UserUpdateResponse(
        Long id,
        String userId,
        String name,
        String email,
        String phoneNumber
) {
}
