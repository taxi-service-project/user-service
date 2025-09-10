package com.example.user_service;

import com.example.user_service.dto.UserCreateRequest;
import com.example.user_service.dto.UserCreateResponse;
import com.example.user_service.dto.UserUpdateRequest;
import com.example.user_service.dto.UserUpdateResponse;
import com.example.user_service.entity.User;
import com.example.user_service.exception.DuplicateEmailException;
import com.example.user_service.exception.DuplicatePhoneNumberException;
import com.example.user_service.exception.UserNotFoundException;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserCreateRequest userCreateRequest;
    private User user;

    @BeforeEach
    void setUp() {
        userCreateRequest = new UserCreateRequest(
                "test@example.com",
                "password123",
                "Test User",
                "01012345678"
        );

        user = User.builder()
                .email("test@example.com")
                .password("password123")
                .name("Test User")
                .phoneNumber("01012345678")
                .build();
    }

    @Test
    @DisplayName("유효한 사용자 정보로 사용자를 생성하면 성공한다")
    void createUser_withValidUserInfo_shouldSucceed() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByPhoneNumber(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        UserCreateResponse response = userService.createUser(userCreateRequest);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.email()).isEqualTo(userCreateRequest.email());
        assertThat(response.name()).isEqualTo(userCreateRequest.name());
        verify(userRepository, times(1)).existsByEmail(userCreateRequest.email());
        verify(userRepository, times(1)).existsByPhoneNumber(userCreateRequest.phoneNumber());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("이미 존재하는 이메일로 사용자를 생성하면 DuplicateEmailException이 발생한다")
    void createUser_withExistingEmail_shouldThrowDuplicateEmailException() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> userService.createUser(userCreateRequest))
                .isInstanceOf(DuplicateEmailException.class)
                .hasMessageContaining("Email already exists");
        verify(userRepository, times(1)).existsByEmail(userCreateRequest.email());
        verify(userRepository, never()).existsByPhoneNumber(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("이미 존재하는 전화번호로 사용자를 생성하면 DuplicatePhoneNumberException이 발생한다")
    void createUser_withExistingPhoneNumber_shouldThrowDuplicatePhoneNumberException() {
        // Given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.existsByPhoneNumber(anyString())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> userService.createUser(userCreateRequest))
                .isInstanceOf(DuplicatePhoneNumberException.class)
                .hasMessageContaining("Phone number already exists");
        verify(userRepository, times(1)).existsByEmail(userCreateRequest.email());
        verify(userRepository, times(1)).existsByPhoneNumber(userCreateRequest.phoneNumber());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("유효한 ID로 사용자를 삭제하면 성공한다")
    void deleteUser_withValidId_shouldSucceed() {
        // Given
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);
        doNothing().when(userRepository).deleteById(userId);

        // When
        userService.deleteUser(userId);

        // Then
        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    @DisplayName("존재하지 않는 ID로 사용자를 삭제하면 UserNotFoundException이 발생한다")
    void deleteUser_withNonExistentId_shouldThrowUserNotFoundException() {
        // Given
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> userService.deleteUser(userId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found with ID: " + userId);
        verify(userRepository, times(1)).existsById(userId);
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("유효한 정보로 사용자를 업데이트하면 성공한다")
    void updateUser_withValidInfo_shouldSucceed() {
        // Given
        Long userId = 1L;
        UserUpdateRequest request = new UserUpdateRequest("Updated Name", "010-9876-5432");
        User existingUser = User.builder()
                .email("test@example.com")
                .password("old_password")
                .name("Old Name")
                .phoneNumber("010-1234-5678")
                .build();
        ReflectionTestUtils.setField(existingUser, "id", userId);

        User updatedUser = User.builder()
                .email("test@example.com")
                .password("old_password") // Password should not change
                .name(request.name())
                .phoneNumber(request.phoneNumber())
                .build();
        ReflectionTestUtils.setField(updatedUser, "id", userId);

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // When
        UserUpdateResponse response = userService.updateUser(userId, request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(userId);
        assertThat(response.name()).isEqualTo(request.name());
        assertThat(response.phoneNumber()).isEqualTo(request.phoneNumber());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("존재하지 않는 ID로 사용자를 업데이트하면 UserNotFoundException이 발생한다")
    void updateUser_withNonExistentId_shouldThrowUserNotFoundException() {
        // Given
        Long userId = 1L;
        UserUpdateRequest request = new UserUpdateRequest("Updated Name", "010-9876-5432");

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.updateUser(userId, request))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found with ID: " + userId);
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }
}
