package com.university.mcmaster.repositories.impl;

import com.google.cloud.firestore.Query;
import com.google.firebase.cloud.FirestoreClient;
import com.university.mcmaster.enums.FilePurpose;
import com.university.mcmaster.enums.RentalUnitElement;
import com.university.mcmaster.models.entities.File;
import com.university.mcmaster.repositories.FileRepo;
import com.university.mcmaster.repositories.utils.FirebaseUtils;
import com.university.mcmaster.utils.Constants;
import com.university.mcmaster.utils.FirestoreConstants;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class FileRepoImpl extends FirebaseUtils<File> implements FileRepo {
    @Override
    public File findById(String fileId) {
        try {
            return FirestoreClient.getFirestore().collection(FirestoreConstants.FS_FILES).document(fileId).get().get().toObject(File.class);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(String fileId, HashMap<String, Object> updateMap) {
        update(fileId,FirestoreConstants.FS_FILES,updateMap);
    }

    @Override
    public List<File> getFilesByRentalUnitIdAndDeletedFalseAndUploadedOnGcpTrue(String rentalUnitId) {
        return processQueryForEntityList(
                FirestoreClient.getFirestore().collection(FirestoreConstants.FS_FILES)
                        .whereEqualTo("rentalUnitId",rentalUnitId)
                        .whereEqualTo("deleted",false)
                        .whereEqualTo("uploadedOnGcp",true)
                ,File.class);
    }

    @Override
    public List<File> getFilesByRentalUnitIdAndRentalUnitElementDeletedFalseAndUploadedOnGcpTrue(String rentalUnitId, RentalUnitElement element) {
        return processQueryForEntityList(
                FirestoreClient.getFirestore().collection(FirestoreConstants.FS_FILES)
                        .whereEqualTo("rentalUnitId",rentalUnitId)
                        .whereEqualTo("deleted",false)
                        .whereEqualTo("rentalUnitElement",element)
                        .whereEqualTo("uploadedOnGcp",true)
                ,File.class);
    }

    @Override
    public List<File> getFilesByRentalUnitIdAndUploadedOnGcpTrueAndDeletedFalse(String rentalUnitId) {
        return processQueryForEntityList(
                FirestoreClient.getFirestore().collection(FirestoreConstants.FS_FILES)
                        .whereEqualTo("rentalUnitId",rentalUnitId)
                        .whereEqualTo("deleted",false)
                        .whereEqualTo("uploadedOnGcp",true)
                        .limit(RentalUnitElement.getTotalAllowedFiles())
                ,File.class
        );
    }

    @Override
    public List<File> getFilesByUserIdAndPurposeLeaseDocAndDeletedFalse(String userId) {
        return processQueryForEntityList(
                FirestoreClient.getFirestore().collection(FirestoreConstants.FS_FILES)
                        .whereEqualTo("userId",userId)
                        .whereEqualTo("purpose", FilePurpose.lease_doc)
                        .whereEqualTo("deleted",false)
                        .whereEqualTo("uploadedOnGcp",true)
                        .orderBy("createdOn", Query.Direction.DESCENDING)
                        .limit(5)
                ,File.class
        );
    }
}
