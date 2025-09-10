package com.example.user_service.dto;

import lombok.Builder;

@Builder
public record PaymentMethodResponse(
        Long id,
        String cardIssuer,
        String cardNumberMasked,
        boolean isDefault
) {
}
