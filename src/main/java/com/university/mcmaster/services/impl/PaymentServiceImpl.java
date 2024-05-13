package com.university.mcmaster.services.impl;

import com.google.gson.JsonSyntaxException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import com.university.mcmaster.enums.Currency;
import com.university.mcmaster.enums.PaymentRequestType;
import com.university.mcmaster.enums.PaymentStatus;
import com.university.mcmaster.enums.UserRole;
import com.university.mcmaster.exceptions.ActionNotAllowedException;
import com.university.mcmaster.exceptions.EntityNotFoundException;
import com.university.mcmaster.exceptions.InvalidParamValueException;
import com.university.mcmaster.exceptions.UnAuthenticatedUserException;
import com.university.mcmaster.integrations.stripe.service.StripePaymentService;
import com.university.mcmaster.models.dtos.request.ApiResponse;
import com.university.mcmaster.models.dtos.request.PaymentRequestDto;
import com.university.mcmaster.models.dtos.response.MethodResponse;
import com.university.mcmaster.models.entities.*;
import com.university.mcmaster.repositories.BankAccountRepo;
import com.university.mcmaster.repositories.PaymentRepo;
import com.university.mcmaster.services.PaymentService;
import com.university.mcmaster.services.RentalUnitService;
import com.university.mcmaster.utils.EnvironmentVariables;
import com.university.mcmaster.utils.Utility;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepo paymentRepo;
    private final BankAccountRepo bankAccountRepo;
    private final RentalUnitService rentalUnitService;

    @Override
    public ResponseEntity<?> createPaymentLink(PaymentRequestDto requestDto, String requestId, HttpServletRequest httpServletRequest) {
        CustomUserDetails userDetails = Utility.customUserDetails(httpServletRequest);
        if(null == userDetails || null == userDetails.getRoles()) throw new UnAuthenticatedUserException();
        if(userDetails.getRoles().contains(UserRole.student)){
            return processPaymentRequestForStudent(userDetails,requestDto,requestId);
        }
        if(userDetails.getRoles().contains(UserRole.rental_unit_owner)){
            return processPaymentRequestForRentalUnitOwner(userDetails,requestDto,requestId);
        }
        throw new UnAuthenticatedUserException();
    }

    private ResponseEntity<?> processPaymentRequestForRentalUnitOwner(CustomUserDetails userDetails, PaymentRequestDto requestDto, String requestId) {
        if(false == PaymentRequestType.isValid(requestDto.getRequestType(),UserRole.rental_unit_owner)) throw new InvalidParamValueException();
        if(PaymentRequestType.payment_for_listing == requestDto.getRequestType()){
            RentalUnit rentalUnit = rentalUnitService.findRentalUnitById(requestDto.getEntityId());
            if(null == rentalUnit) throw new EntityNotFoundException();
            MethodResponse<String,String,String> checkoutSessionRes = StripePaymentService.createCheckoutSession(userDetails.getEmail(),rentalUnit.getStripeProduct().getStripeProductId());
            Payment payment = Payment.builder()
                    .rentalUnitId(rentalUnit.getId())
                    .createdOn(Instant.now().toEpochMilli())
                    .userId(userDetails.getId())
                    .id(UUID.randomUUID().toString())
                    .type(requestDto.getRequestType())
                    .paymentStatus(PaymentStatus.created)
                    .stripeMetaData(StripeMetaData.builder()
                            .checkoutSessionId(checkoutSessionRes.getResult_2())
                            .paymentIntentId(checkoutSessionRes.getResult_3())
                            .build())
                    .build();
            boolean res = paymentRepo.save(payment);
            return ResponseEntity.ok(ApiResponse.builder()
                            .data(checkoutSessionRes.getResult_1())
                    .build());
        }
        throw new InvalidParamValueException("requestType");
    }

    private ResponseEntity<?> processPaymentRequestForStudent(CustomUserDetails userDetails, PaymentRequestDto requestDto, String requestId) {
        if(false == PaymentRequestType.isValid(requestDto.getRequestType(),UserRole.student)) throw new InvalidParamValueException();
        if(PaymentRequestType.payment_for_deposit == requestDto.getRequestType()){
            RentalUnit rentalUnit = rentalUnitService.findRentalUnitById(requestDto.getEntityId());
            if(null == rentalUnit) throw new EntityNotFoundException();
            if(null == rentalUnit.getStripeProduct()) {
                MethodResponse<StripeProduct,?,?> productRes = StripePaymentService.createProductAndPrice(rentalUnit.getTitle(),rentalUnit.getDescription(), Currency.usd,rentalUnit.getDeposit() * 100);
                if(productRes.isFlag()) throw new ActionNotAllowedException();
                rentalUnit.setStripeProduct(productRes.getResult_1());
                rentalUnitService.updateRentalUnit(rentalUnit.getId(),new HashMap<String, Object>(){{
                    put("stripeProduct",rentalUnit.getStripeProduct());
                }});
            }
            MethodResponse<String,String,String> checkoutSessionRes = StripePaymentService.createCheckoutSession(userDetails.getEmail(),rentalUnit.getStripeProduct().getStripeProductId());
            Payment payment = Payment.builder()
                    .rentalUnitId(rentalUnit.getId())
                    .createdOn(Instant.now().toEpochMilli())
                    .userId(userDetails.getId())
                    .id(UUID.randomUUID().toString())
                    .type(requestDto.getRequestType())
                    .paymentStatus(PaymentStatus.created)
                    .stripeMetaData(StripeMetaData.builder()
                            .checkoutSessionId(checkoutSessionRes.getResult_2())
                            .paymentIntentId(checkoutSessionRes.getResult_3())
                            .build())
                    .build();
            boolean res = paymentRepo.save(payment);
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(checkoutSessionRes.getResult_1())
                    .build());
        }
        throw new InvalidParamValueException("requestType");
    }

    @Override
    public ResponseEntity<?> processPaymentWebhook(String payload,String sigHeader) {
        Event event = null;
        try {
            event = Webhook.constructEvent(payload, sigHeader, EnvironmentVariables.STRIPE_ENDPOINT_SECRET);
            System.out.println(event.getType());
            if("checkout.session.async_payment_succeeded".equals(event.getType())){}
            if("payment_intent.succeeded".equals(event.getType())){}
            if("checkout.session.async_payment_succeeded".equals(event.getType())){}
        } catch (JsonSyntaxException | SignatureVerificationException e) {
            return ResponseEntity.badRequest().body("");
        }
        return ResponseEntity.ok("ok");
    }
}
