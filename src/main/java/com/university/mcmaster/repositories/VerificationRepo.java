package com.university.mcmaster.repositories;

import com.university.mcmaster.models.entities.SheerIdVerificationData;

public interface VerificationRepo {
    boolean save(SheerIdVerificationData verificationData);
    SheerIdVerificationData getLatestSheerIdVerificationByEmailAndStatusSuccess(String email);
}
