package com.example.user_service.dto.response;
import com.example.user_service.entity.User;

public record InternalUserResponse(
        String userId,
        String name
) {
    public static InternalUserResponse fromEntity(User user) {
        return new InternalUserResponse(user.getUserId(), user.getName());
    }
}
