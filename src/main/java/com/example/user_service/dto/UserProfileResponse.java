package com.example.user_service.dto;

public record UserProfileResponse(
        Long id,
        String email,
        String name,
        String phoneNumber
) {
}