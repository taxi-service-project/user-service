package com.example.user_service.controller;

import com.example.user_service.dto.response.InternalUserResponse;
import com.example.user_service.dto.response.UserInfoForPaymentResponse;
import com.example.user_service.service.PaymentMethodService;
import com.example.user_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/api/users")
@RequiredArgsConstructor
public class InternalUserController {

    private final UserService userService;
    private final PaymentMethodService paymentMethodService;

    @GetMapping("/{userId}")
    public ResponseEntity<InternalUserResponse> getUserInfo(@PathVariable String userId) {
        InternalUserResponse response = userService.getUserByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userId}/payment-methods/default")
    public ResponseEntity<UserInfoForPaymentResponse> getUserInfoForPayment(@PathVariable String userId) {
        UserInfoForPaymentResponse response = paymentMethodService.getDefaultPaymentMethod(userId);
        return ResponseEntity.ok(response);
    }

}