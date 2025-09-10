package com.example.user_service.dto;

import lombok.Builder;

@Builder
public record PaymentMethodRegisterResponse(
        Long id,
        String cardIssuer,
        String cardNumberMasked,
        boolean isDefault
) {
}
