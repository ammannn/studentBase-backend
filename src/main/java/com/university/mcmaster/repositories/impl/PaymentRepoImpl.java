package com.university.mcmaster.repositories.impl;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import com.university.mcmaster.models.entities.Payment;
import com.university.mcmaster.repositories.PaymentRepo;
import com.university.mcmaster.repositories.utils.FirebaseUtils;
import com.university.mcmaster.utils.FirestoreConstants;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

@Repository
public class PaymentRepoImpl extends FirebaseUtils<Payment> implements PaymentRepo {
    @Override
    public Payment findById(String paymentId) {
        try {
            return FirestoreClient.getFirestore().collection(FirestoreConstants.FS_PAYMENTS)
                    .document(paymentId)
                    .get().get().toObject(Payment.class);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean update(String paymentId, HashMap<String, Object> updateMap) {
        ApiFuture<WriteResult> future = FirestoreClient.getFirestore().collection(FirestoreConstants.FS_PAYMENTS)
                .document(paymentId)
                .update(updateMap);
        try {
            future.get();
            return true;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return false;
    }
}
