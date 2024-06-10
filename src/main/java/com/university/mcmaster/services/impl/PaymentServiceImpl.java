package com.university.mcmaster.services.impl;

import com.google.gson.JsonSyntaxException;
import com.stripe.exception.EventDataObjectDeserializationException;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.university.mcmaster.enums.*;
import com.university.mcmaster.exceptions.*;
import com.university.mcmaster.integrations.stripe.service.StripePaymentService;
import com.university.mcmaster.models.dtos.request.ApiResponse;
import com.university.mcmaster.models.dtos.request.PaymentRequestDto;
import com.university.mcmaster.models.dtos.response.MethodResponse;
import com.university.mcmaster.models.entities.*;
import com.university.mcmaster.repositories.AdminRepo;
import com.university.mcmaster.repositories.BankAccountRepo;
import com.university.mcmaster.repositories.PaymentRepo;
import com.university.mcmaster.services.ApplicationService;
import com.university.mcmaster.services.PaymentService;
import com.university.mcmaster.services.RentalUnitService;
import com.university.mcmaster.services.UserService;
import com.university.mcmaster.utils.EnvironmentVariables;
import com.university.mcmaster.utils.Utility;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);
    private final PaymentRepo paymentRepo;
    private final BankAccountRepo bankAccountRepo;
    private final RentalUnitService rentalUnitService;
    private final AdminRepo adminRepo;
    private final ApplicationService applicationService;
    private final UserService userService;

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
            String paymentId = UUID.randomUUID().toString();
            AdminConfig adminConfig = adminRepo.getAdminConfig();
            MethodResponse<String,String,String> checkoutSessionRes = StripePaymentService.createCheckoutSession(
                    userDetails.getEmail(),adminConfig.getStripeProductIdForListing(),new HashMap<String, String>(){{
                        put("paymentId",paymentId);
                        put("requestId",requestId);
                    }}, Utility.createPaymentPageUrl(PaymentRequestType.payment_for_listing.toString(),"rental_unit",PaymentStatus.successful.toString(),rentalUnit.getId()),
                    Utility.createPaymentPageUrl(PaymentRequestType.payment_for_listing.toString(),"rental_unit",PaymentStatus.failed.toString(),rentalUnit.getId())
            );
            if(false == checkoutSessionRes.isFlag()) {
                log.trace("creating payment object with intentId : " + checkoutSessionRes.getResult_3());
                Payment payment = Payment.builder()
                        .rentalUnitId(rentalUnit.getId())
                        .createdOn(Instant.now().toEpochMilli())
                        .userId(userDetails.getId())
                        .id(paymentId)
                        .amount(adminConfig.getStripeProductAmountForListing())
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
            throw new FailedToPerformOperation();
        }
        throw new InvalidParamValueException("requestType");
    }

    private ResponseEntity<?> processPaymentRequestForStudent(CustomUserDetails userDetails, PaymentRequestDto requestDto, String requestId) {
        if(false == PaymentRequestType.isValid(requestDto.getRequestType(),UserRole.student)) throw new InvalidParamValueException();
        if(PaymentRequestType.payment_for_deposit == requestDto.getRequestType()){
            Application application = applicationService.getApplicationById(requestDto.getEntityId());
            if(null == application) throw new EntityNotFoundException();
            if(ApplicationStatus.lease_signed != application.getApplicationStatus()) throw new ActionNotAllowedException("create_payment","payment cannot be done before lease is signed");
            RentalUnit rentalUnit = rentalUnitService.findRentalUnitById(application.getRentalUnitId());
            if(null == rentalUnit) throw new EntityNotFoundException();
            if(null == rentalUnit.getStripeProduct()) {
                MethodResponse<StripeProduct,?,?> productRes = StripePaymentService.createProductAndPrice(rentalUnit.getTitle(),rentalUnit.getDescription(), rentalUnit.getDeposit().getCurrency(), rentalUnit.getDeposit().getAmount() * 100);
                if(productRes.isFlag()) throw new ActionNotAllowedException();
                rentalUnit.setStripeProduct(productRes.getResult_1());
                rentalUnitService.updateRentalUnit(rentalUnit.getId(),new HashMap<String, Object>(){{
                    put("stripeProduct",rentalUnit.getStripeProduct());
                }});
            }
            String paymentId =UUID.randomUUID().toString();
            MethodResponse<String,String,String> checkoutSessionRes = StripePaymentService.createCheckoutSession(
                    userDetails.getEmail(),rentalUnit.getStripeProduct().getStripeProductId(),new HashMap<String, String>(){{
                        put("paymentId",paymentId);
                        put("requestId",requestId);
                    }}, Utility.createPaymentPageUrl(PaymentRequestType.payment_for_deposit.toString(),"application",PaymentStatus.successful.toString(),application.getId()),
                    Utility.createPaymentPageUrl(PaymentRequestType.payment_for_deposit.toString(),"application",PaymentStatus.failed.toString(),application.getId())
            );
            Payment payment = Payment.builder()
                    .rentalUnitId(rentalUnit.getId())
                    .createdOn(Instant.now().toEpochMilli())
                    .userId(userDetails.getId())
                    .id(paymentId)
                    .applicationId(application.getId())
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
        System.out.println("**************************************************");
        log.trace("request is received to process payment webhook");
        Event event = null;
        try {
            event = Webhook.constructEvent(payload, sigHeader, EnvironmentVariables.STRIPE_ENDPOINT_SECRET);
            EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
            StripeObject stripeObject  = dataObjectDeserializer.deserializeUnsafe();
            System.out.println(event.getType());
            if("payment_intent.succeeded".equals(event.getType())){
                PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                processPaymentSuccess(paymentIntent);
            }else if(
                    "payment_intent.payment_failed".equals(event.getType()) ||
                    "payment_intent.canceled".equals(event.getType())
            ){
                PaymentIntent paymentIntent = (PaymentIntent) stripeObject;
                processPaymentFailed(paymentIntent);
            } else if("checkout.session.expired".equals(event.getType())){
                Session session = (Session) stripeObject;
                processExpiredCheckoutSession(session);
            }
        } catch (JsonSyntaxException | SignatureVerificationException | EventDataObjectDeserializationException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("");
        }
        System.out.println("**************************************************");
        return ResponseEntity.ok("ok");
    }

    private void processExpiredCheckoutSession(Session session) {
        Map<String,String> paymentIntentData =  session.getMetadata();
        String requestId = paymentIntentData.get("requestId");
        String paymentId = paymentIntentData.get("paymentId");
        Payment payment = paymentRepo.findById(paymentId);
        boolean updated = paymentRepo.update(paymentId,new HashMap<String,Object>(){{
            put("paymentStatus",PaymentStatus.expired);
            put("eligibleForListing",false);
        }});
        if(PaymentRequestType.payment_for_listing == payment.getType()) {
            rentalUnitService.updateRentalUnit(payment.getRentalUnitId(),new HashMap<String, Object>(){{
                put("paymentStatus",PaymentStatus.expired);
                put("eligibleForListing",false);
            }});
        }
    }

    private void processPaymentFailed(PaymentIntent paymentIntent) {
        log.trace("processing stripe webhook for failed payment");
        Map<String,String> paymentIntentData =  paymentIntent.getMetadata();
        String requestId = paymentIntentData.get("requestId");
        String paymentId = paymentIntentData.get("paymentId");
        Payment payment = paymentRepo.findById(paymentId);
        boolean updated = paymentRepo.update(paymentId,new HashMap<String,Object>(){{
            put("paymentStatus",PaymentStatus.failed);
            put("stripeMetaData.paymentIntentId",paymentIntent.getId());
            put("eligibleForListing",false);
        }});

        if(PaymentRequestType.payment_for_listing == payment.getType()) {
            rentalUnitService.updateRentalUnit(payment.getRentalUnitId(),new HashMap<String, Object>(){{
                put("paymentStatus",PaymentStatus.failed);
                put("eligibleForListing",false);
            }});
        } else if(PaymentRequestType.payment_for_deposit == payment.getType()) {
            Application application = applicationService.getApplicationById(payment.getApplicationId());
            applicationService.updateApplication(application.getId(),new HashMap<String, Object>(){{
                put("applicationStatus",ApplicationStatus.payment_failed);
            }});
        }
    }

    private void processPaymentSuccess(PaymentIntent paymentIntent) {
        log.trace("processing stripe webhook for successfull payment");
        Map<String,String> paymentIntentData =  paymentIntent.getMetadata();
        String requestId = paymentIntentData.get("requestId");
        String paymentId = paymentIntentData.get("paymentId");
        Payment payment = paymentRepo.findById(paymentId);
        boolean updated = paymentRepo.update(paymentId,new HashMap<String,Object>(){{
            put("paymentStatus",PaymentStatus.successful);
            put("stripeMetaData.paymentIntentId",paymentIntent.getId());
            put("eligibleForListing",true);
        }});
        RentalUnit rentalUnit = rentalUnitService.findRentalUnitById(payment.getRentalUnitId());
        if(PaymentRequestType.payment_for_listing == payment.getType()) {
            rentalUnitService.updateRentalUnit(payment.getRentalUnitId(),new HashMap<String, Object>(){{
                put("paymentStatus",PaymentStatus.successful);
                put("eligibleForListing",VerificationStatus.failed != rentalUnit.getVerificationStatus());
            }});
        } else if(PaymentRequestType.payment_for_deposit == payment.getType()) {
            Application application = applicationService.getApplicationById(payment.getApplicationId());
            applicationService.updateApplication(application.getId(),new HashMap<String, Object>(){{
                put("applicationStatus",ApplicationStatus.payment_done);
            }});
            rentalUnitService.updateRentalUnit(payment.getRentalUnitId(),new HashMap<String, Object>(){{
                put("rentalUnitStatus",RentalUnitStatus.rented);
                put("eligibleForListing",false);
            }});
            userService.incrementOrDecrementDashboardCounts(rentalUnit.getUserId(),"depositReceived",rentalUnit.getDeposit().getAmount(),"inc");
        }
    }
}
