package com.university.mcmaster.repositories;

import com.university.mcmaster.models.entities.Payment;

public interface PaymentRepo {
    boolean save(Payment payment);
}
