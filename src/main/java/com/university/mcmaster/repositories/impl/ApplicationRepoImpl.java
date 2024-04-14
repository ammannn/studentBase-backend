package com.university.mcmaster.repositories.impl;

import com.google.firebase.cloud.FirestoreClient;
import com.university.mcmaster.models.entities.Application;
import com.university.mcmaster.repositories.ApplicationRepo;
import com.university.mcmaster.repositories.utils.FirebaseUtils;
import com.university.mcmaster.utils.FirestoreConstants;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ExecutionException;

@Repository
public class ApplicationRepoImpl extends FirebaseUtils<Application> implements ApplicationRepo {
    @Override
    public Application findById(String applicationId) {
        try {
            return FirestoreClient.getFirestore().collection(FirestoreConstants.FS_APPLICATIONS)
                    .document(applicationId)
                    .get().get().toObject(Application.class);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
