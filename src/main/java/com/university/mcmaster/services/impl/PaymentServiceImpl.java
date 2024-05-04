package com.university.mcmaster.services.impl;

import com.university.mcmaster.repositories.BankAccountRepo;
import com.university.mcmaster.repositories.PaymentRepo;
import com.university.mcmaster.services.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepo paymentRepo;
    private final BankAccountRepo bankAccountRepo;
}
