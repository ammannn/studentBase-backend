package com.university.mcmaster.controllers;

import com.university.mcmaster.services.AuthService;
import com.university.mcmaster.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/wh")
@RequiredArgsConstructor
public class WebhookController {

    private final PaymentService paymentService;
    private final AuthService authService;

    @PostMapping("/payment-update")
    public ResponseEntity<?> updatePaymentStatus(
            @RequestBody String request,
            @RequestHeader("Stripe-Signature") String sigHeader
    ){
        return paymentService.processPaymentWebhook(request,sigHeader);
    }

    @PostMapping("/sheerid-verification")
    public ResponseEntity<?> handleSheerIdVerification(
            @RequestBody Map<String,Object> requestDto,
            @RequestHeader(name = "x-sheerid-signature",required = false) String sigHeader
    ){
        return authService.handleSheerIdVerification(requestDto,sigHeader);
    }

}
