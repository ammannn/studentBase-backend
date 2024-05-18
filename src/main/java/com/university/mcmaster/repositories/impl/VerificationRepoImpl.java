package com.university.mcmaster.repositories.impl;

import com.google.firebase.cloud.FirestoreClient;
import com.university.mcmaster.models.entities.SheerIdVerificationData;
import com.university.mcmaster.repositories.VerificationRepo;
import com.university.mcmaster.repositories.utils.FirebaseUtils;
import com.university.mcmaster.utils.FirestoreConstants;

import javax.management.Query;

public class VerificationRepoImpl extends FirebaseUtils<SheerIdVerificationData> implements VerificationRepo {
    @Override
    public SheerIdVerificationData getLatestSheerIdVerificationByEmailAndStatusSuccess(String email) {
        return processQueryForEntity(FirestoreClient.getFirestore()
                .collection(FirestoreConstants.VERIFICATION_DATA)
                .whereEqualTo("email",email)
                .whereEqualTo("status","success")
                .orderBy("createdOn", com.google.cloud.firestore.Query.Direction.DESCENDING)
                .limit(1),SheerIdVerificationData.class);
    }
}
