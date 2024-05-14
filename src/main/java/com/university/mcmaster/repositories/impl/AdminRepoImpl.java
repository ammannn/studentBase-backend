package com.university.mcmaster.repositories.impl;

import com.google.firebase.cloud.FirestoreClient;
import com.university.mcmaster.models.entities.AdminConfig;
import com.university.mcmaster.repositories.AdminRepo;
import com.university.mcmaster.utils.Constants;
import com.university.mcmaster.utils.FirestoreConstants;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

@Repository
public class AdminRepoImpl implements AdminRepo {
    @Override
    public AdminConfig getAdminConfig() {
        try {
            return FirestoreClient.getFirestore()
                    .collection(FirestoreConstants.ADMIN_WILD_DOCS)
                    .document(Constants.ADMIN_CONFIG_DOC_ID)
                    .get().get().toObject(AdminConfig.class);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void saveAdminConfig(AdminConfig data) {
        FirestoreClient.getFirestore()
                .collection(FirestoreConstants.ADMIN_WILD_DOCS)
                .document(data.getId())
                .set(data);
    }

    @Override
    public void updateAdminConfig(HashMap<String, Object> updateMap) {
        FirestoreClient.getFirestore()
                .collection(FirestoreConstants.ADMIN_WILD_DOCS)
                .document(Constants.ADMIN_CONFIG_DOC_ID)
                .update(updateMap);
    }
}
