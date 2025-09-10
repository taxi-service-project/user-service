package com.example.user_service.controller;

import com.example.user_service.dto.PaymentMethodRegisterRequest;
import com.example.user_service.dto.PaymentMethodRegisterResponse;
import com.example.user_service.service.PaymentMethodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users/{userId}/payment-methods")
@RequiredArgsConstructor
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    @PostMapping
    public ResponseEntity<PaymentMethodRegisterResponse> registerPaymentMethod(
            @PathVariable Long userId,
            @Valid @RequestBody PaymentMethodRegisterRequest request) {
        PaymentMethodRegisterResponse response = paymentMethodService.registerPaymentMethod(userId, request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
