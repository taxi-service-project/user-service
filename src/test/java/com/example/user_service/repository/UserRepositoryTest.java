package com.example.user_service.repository;

import com.example.user_service.entity.User;
import com.example.user_service.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    @DisplayName("이메일로 사용자가 존재하는지 확인하면 true를 반환한다")
    void existsByEmail_whenUserExists_returnsTrue() {
        // Given
        User user = User.builder()
                .email("test@example.com")
                .password("password")
                .name("Test User")
                .phoneNumber("01012345678")
                .build();
        entityManager.persistAndFlush(user);

        // When
        boolean exists = userRepository.existsByEmail("test@example.com");

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("이메일로 사용자가 존재하지 않는지 확인하면 false를 반환한다")
    void existsByEmail_whenUserDoesNotExist_returnsFalse() {
        // Given
        // No user persisted

        // When
        boolean exists = userRepository.existsByEmail("nonexistent@example.com");

        // Then
        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("전화번호로 사용자가 존재하는지 확인하면 true를 반환한다")
    void existsByPhoneNumber_whenUserExists_returnsTrue() {
        // Given
        User user = User.builder()
                .email("test2@example.com")
                .password("password")
                .name("Test User 2")
                .phoneNumber("01087654321")
                .build();
        entityManager.persistAndFlush(user);

        // When
        boolean exists = userRepository.existsByPhoneNumber("01087654321");

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("전화번호로 사용자가 존재하지 않는지 확인하면 false를 반환한다")
    void existsByPhoneNumber_whenUserDoesNotExist_returnsFalse() {
        // Given
        // No user persisted

        // When
        boolean exists = userRepository.existsByPhoneNumber("01099998888");

        // Then
        assertThat(exists).isFalse();
    }
}
