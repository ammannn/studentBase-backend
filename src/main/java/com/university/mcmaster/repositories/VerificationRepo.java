package com.university.mcmaster.repositories;

import com.university.mcmaster.integrations.sheerid.model.SheerIdVerificationData;
import com.university.mcmaster.integrations.sheerid.model.SheerIdVerificationDetails;

public interface VerificationRepo {
    boolean save(SheerIdVerificationDetails verificationData);

    SheerIdVerificationDetails getLatestSheerIdVerificationDetailsByEmail(String email);
}
