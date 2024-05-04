package com.university.mcmaster.repositories.impl;

import com.university.mcmaster.models.entities.Payment;
import com.university.mcmaster.repositories.PaymentRepo;
import com.university.mcmaster.repositories.utils.FirebaseUtils;
import org.springframework.stereotype.Repository;

@Repository
public class PaymentRepoImpl extends FirebaseUtils<Payment> implements PaymentRepo {
}
