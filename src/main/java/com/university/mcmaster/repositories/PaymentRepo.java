package com.university.mcmaster.repositories;

import com.university.mcmaster.models.entities.Payment;

import java.util.HashMap;

public interface PaymentRepo {
    boolean save(Payment payment);

    Payment findById(String paymentId);

    boolean update(String paymentId, HashMap<String, Object> hashMap);
}
