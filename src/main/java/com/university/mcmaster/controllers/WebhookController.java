package com.university.mcmaster.controllers;

import com.university.mcmaster.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wh")
@RequiredArgsConstructor
public class WebhookController {

    private final PaymentService paymentService;

    @PostMapping("/payment-update")
    public ResponseEntity<?> updatePaymentStatus(
            @RequestBody String request,
            @RequestHeader("Stripe-Signature") String sigHeader
    ){
        return paymentService.processPaymentWebhook(request,sigHeader);
    }

}
