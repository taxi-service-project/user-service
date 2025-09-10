package com.example.user_service.service;

import com.example.user_service.dto.UserCreateRequest;
import com.example.user_service.dto.UserCreateResponse;
import com.example.user_service.dto.UserUpdateRequest;
import com.example.user_service.dto.UserUpdateResponse;
import com.example.user_service.entity.User;
import com.example.user_service.exception.DuplicateEmailException;
import com.example.user_service.exception.DuplicatePhoneNumberException;
import com.example.user_service.repository.UserRepository;
import com.example.user_service.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserCreateResponse createUser(UserCreateRequest request) {
        log.info("이메일: {} 및 전화번호: {} 로 사용자 생성을 시도합니다.", request.email(), request.phoneNumber());

        if (userRepository.existsByEmail(request.email())) {
            log.warn("사용자 생성 실패: 이메일 {} 이(가) 이미 존재합니다.", request.email());
            throw new DuplicateEmailException("Email already exists: " + request.email());
        }

        if (userRepository.existsByPhoneNumber(request.phoneNumber())) {
            log.warn("사용자 생성 실패: 전화번호 {} 이(가) 이미 존재합니다.", request.phoneNumber());
            throw new DuplicatePhoneNumberException("Phone number already exists: " + request.phoneNumber());
        }

        User newUser = User.builder()
                .email(request.email())
                .password(request.password())
                .name(request.name())
                .phoneNumber(request.phoneNumber())
                .build();

        User savedUser = userRepository.save(newUser);
        log.info("사용자 ID: {} 로 사용자 생성 성공.", savedUser.getId());

        return new UserCreateResponse(savedUser.getId(), savedUser.getEmail(), savedUser.getName());
    }

    @Transactional
    public void deleteUser(Long id) {
        log.info("ID: {} 로 사용자 삭제를 시도합니다.", id);
        if (!userRepository.existsById(id)) {
            log.warn("사용자 삭제 실패: ID {} 에 해당하는 사용자를 찾을 수 없습니다.", id);
            throw new UserNotFoundException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
        log.info("사용자 ID: {} 삭제 성공.", id);
    }

    public UserUpdateResponse updateUser(Long id, UserUpdateRequest request) {
        return userRepository.findById(id)
                             .map(user -> {
                                 user.update(request.name(), request.phoneNumber());
                                 User updatedUser = userRepository.save(user);
                                 log.info("사용자 ID: {} 수정 성공.", id);
                                 return new UserUpdateResponse(updatedUser.getId(), updatedUser.getName(),
                                         updatedUser.getEmail(), updatedUser.getPhoneNumber());
                             })
                             .orElseThrow(() -> {
                                 log.warn("사용자 수정 실패: ID {} 에 해당하는 사용자를 찾을 수 없습니다.",
                                         id);
                                 return new UserNotFoundException("User not found with ID: " + id);
                             });
    }
}
