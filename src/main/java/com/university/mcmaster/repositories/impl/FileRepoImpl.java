package com.university.mcmaster.repositories.impl;

import com.google.firebase.cloud.FirestoreClient;
import com.university.mcmaster.models.entities.File;
import com.university.mcmaster.repositories.FileRepo;
import com.university.mcmaster.repositories.utils.FirebaseUtils;
import com.university.mcmaster.utils.FirestoreConstants;
import org.springframework.stereotype.Service;

import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
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
}
