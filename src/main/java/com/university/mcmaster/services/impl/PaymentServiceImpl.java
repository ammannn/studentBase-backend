package com.university.mcmaster.services.impl;

import com.university.mcmaster.enums.PaymentRequestType;
import com.university.mcmaster.enums.UserRole;
import com.university.mcmaster.exceptions.InvalidParamValueException;
import com.university.mcmaster.exceptions.UnAuthenticatedUserException;
import com.university.mcmaster.models.dtos.request.PaymentRequestDto;
import com.university.mcmaster.models.entities.CustomUserDetails;
import com.university.mcmaster.repositories.BankAccountRepo;
import com.university.mcmaster.repositories.PaymentRepo;
import com.university.mcmaster.services.PaymentService;
import com.university.mcmaster.utils.Utility;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepo paymentRepo;
    private final BankAccountRepo bankAccountRepo;

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
        return null;
    }

    private ResponseEntity<?> processPaymentRequestForStudent(CustomUserDetails userDetails, PaymentRequestDto requestDto, String requestId) {
        if(false == PaymentRequestType.isValid(requestDto.getRequestType(),UserRole.student)) throw new InvalidParamValueException();
        return null;
    }
}
