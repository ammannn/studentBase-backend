package com.university.mcmaster.repositories.impl;

import com.google.firebase.cloud.FirestoreClient;
import com.university.mcmaster.integrations.sheerid.model.SheerIdVerificationData;
import com.university.mcmaster.integrations.sheerid.model.SheerIdVerificationDetails;
import com.university.mcmaster.repositories.VerificationRepo;
import com.university.mcmaster.repositories.utils.FirebaseUtils;
import com.university.mcmaster.utils.FirestoreConstants;
import org.springframework.stereotype.Repository;

@Repository
public class VerificationRepoImpl extends FirebaseUtils<SheerIdVerificationDetails> implements VerificationRepo {
    @Override
    public SheerIdVerificationDetails getLatestSheerIdVerificationDetailsByEmail(String email) {
        return processQueryForEntity(FirestoreClient.getFirestore()
                .collection(FirestoreConstants.VERIFICATION_DATA)
                .whereEqualTo("personInfo.email",email)
                .orderBy("created", com.google.cloud.firestore.Query.Direction.DESCENDING)
                .limit(1),SheerIdVerificationDetails.class);
    }
//    @Override
//    public SheerIdVerificationData getLatestSheerIdVerificationByEmailAndStatusSuccess(String email) {
//        return processQueryForEntity(FirestoreClient.getFirestore()
//                .collection(FirestoreConstants.VERIFICATION_DATA)
//                .whereEqualTo("email",email)
//                .whereEqualTo("status","success")
//                .orderBy("createdOn", com.google.cloud.firestore.Query.Direction.DESCENDING)
//                .limit(1),SheerIdVerificationData.class);
//    }
}
