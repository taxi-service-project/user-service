package com.example.user_service.service;

import com.example.user_service.dto.UserCreateRequest;
import com.example.user_service.dto.UserCreateResponse;
import com.example.user_service.entity.User;
import com.example.user_service.exception.DuplicateEmailException;
import com.example.user_service.exception.DuplicatePhoneNumberException;
import com.example.user_service.repository.UserRepository;
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
}
