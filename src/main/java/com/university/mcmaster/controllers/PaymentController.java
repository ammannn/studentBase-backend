package com.university.mcmaster.controllers;

import com.university.mcmaster.models.dtos.request.PaymentRequestDto;
import com.university.mcmaster.services.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/payment")
    public ResponseEntity<?> createPaymentLink(
            @RequestBody PaymentRequestDto requestDto,
            @RequestHeader("requestId") String requestId,
            HttpServletRequest httpServletRequest
    ){
        return paymentService.createPaymentLink(requestDto,requestId,httpServletRequest);
    }


}
