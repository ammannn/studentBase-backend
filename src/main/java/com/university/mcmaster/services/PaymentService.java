package com.university.mcmaster.services;

import com.university.mcmaster.models.dtos.request.PaymentRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface PaymentService {
    ResponseEntity<?> createPaymentLink(PaymentRequestDto requestDto, String requestId, HttpServletRequest httpServletRequest);

    ResponseEntity<?> processPaymentWebhook(String request,String sigHeader);
}
