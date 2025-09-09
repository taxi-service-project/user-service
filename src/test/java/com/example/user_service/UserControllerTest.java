package com.example.user_service;

import com.example.user_service.controller.UserController;
import com.example.user_service.dto.UserCreateRequest;
import com.example.user_service.dto.UserCreateResponse;
import com.example.user_service.exception.UserNotFoundException;
import com.example.user_service.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    @DisplayName("유효한 사용자 정보로 사용자 생성 요청을 보내면 201 Created 응답을 받는다")
    void createUser_withValidUserInfo_returns201Created() throws Exception {
        // Given
        UserCreateRequest request = new UserCreateRequest(
                "test@example.com",
                "password123",
                "Test User",
                "01012345678"
        );
        UserCreateResponse response = new UserCreateResponse(1L, "test@example.com", "Test User");

        when(userService.createUser(any(UserCreateRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.name").value("Test User"));
    }

    @Test
    @DisplayName("유효한 ID로 사용자 삭제 요청을 보내면 204 No Content 응답을 받는다")
    void deleteUser_withValidId_returns204NoContent() throws Exception {
        // Given
        Long userId = 1L;
        doNothing().when(userService).deleteUser(userId);

        // When & Then
        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isNoContent());

        verify(userService, times(1)).deleteUser(userId);
    }

    @Test
    @DisplayName("존재하지 않는 ID로 사용자 삭제 요청을 보내면 404 Not Found 응답을 받는다")
    void deleteUser_withNonExistentId_returns404NotFound() throws Exception {
        // Given
        Long userId = 1L;
        doThrow(new UserNotFoundException("User not found")).when(userService).deleteUser(userId);

        // When & Then
        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).deleteUser(userId);
    }
}
