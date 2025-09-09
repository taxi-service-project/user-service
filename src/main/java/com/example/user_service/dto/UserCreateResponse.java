package com.example.user_service.dto;

public record UserCreateResponse(
    Long id,
    String email,
    String name
) {}
