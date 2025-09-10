package com.example.user_service.controller;

import com.example.user_service.dto.PaymentMethodRegisterRequest;
import com.example.user_service.dto.PaymentMethodRegisterResponse;
import com.example.user_service.dto.PaymentMethodResponse;
import com.example.user_service.service.PaymentMethodService;
import com.example.user_service.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentMethodController.class)
class PaymentMethodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PaymentMethodService paymentMethodService;

    @MockitoBean
    private UserService userService;

    @Test
    @DisplayName("유효한 결제 수단 등록 요청을 보내면 201 Created 응답을 받는다")
    void registerPaymentMethod_withValidRequest_returns201Created() throws Exception {
        // Given
        Long userId = 1L;
        PaymentMethodRegisterRequest request = PaymentMethodRegisterRequest.builder()
                .cardNumber("1234-5678-1234-5678")
                .expiryDate("12/25")
                .cvc("123")
                .build();

        PaymentMethodRegisterResponse expectedResponse = PaymentMethodRegisterResponse.builder()
                .id(1L)
                .cardIssuer("Visa")
                .cardNumberMasked("1234-XXXX-XXXX-5678")
                .isDefault(true)
                .build();

        when(paymentMethodService.registerPaymentMethod(eq(userId), any(PaymentMethodRegisterRequest.class)))
                .thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(post("/api/users/{userId}/payment-methods", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.cardIssuer").value("Visa"))
                .andExpect(jsonPath("$.cardNumberMasked").value("1234-XXXX-XXXX-5678"))
                .andExpect(jsonPath("$.isDefault").value(true));

        verify(paymentMethodService, times(1)).registerPaymentMethod(eq(userId), any(PaymentMethodRegisterRequest.class));
    }

    @Test
    @DisplayName("유효한 사용자 ID로 결제 수단 목록 조회 요청을 보내면 200 OK 응답과 목록을 받는다")
    void getPaymentMethods_withValidUserId_returns200OkAndList() throws Exception {
        // Given
        Long userId = 1L;
        List<PaymentMethodResponse> expectedResponse = Arrays.asList(
                PaymentMethodResponse.builder().id(1L).cardIssuer("Visa").cardNumberMasked("1234-XXXX-XXXX-1111").isDefault(true).build(),
                PaymentMethodResponse.builder().id(2L).cardIssuer("MasterCard").cardNumberMasked("5678-XXXX-XXXX-2222").isDefault(false).build()
        );

        when(paymentMethodService.getPaymentMethods(eq(userId))).thenReturn(expectedResponse);

        // When & Then
        mockMvc.perform(get("/api/users/{userId}/payment-methods", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].cardIssuer").value("Visa"))
                .andExpect(jsonPath("$[0].cardNumberMasked").value("1234-XXXX-XXXX-1111"))
                .andExpect(jsonPath("$[0].isDefault").value(true))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].cardIssuer").value("MasterCard"))
                .andExpect(jsonPath("$[1].cardNumberMasked").value("5678-XXXX-XXXX-2222"))
                .andExpect(jsonPath("$[1].isDefault").value(false));

        verify(paymentMethodService, times(1)).getPaymentMethods(eq(userId));
    }
}