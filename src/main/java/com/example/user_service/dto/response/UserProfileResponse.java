package com.example.user_service.dto.response;

public record UserProfileResponse(
        Long id,
        String userId,
        String email,
        String name,
        String phoneNumber
) {
}