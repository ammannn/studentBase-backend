package com.university.mcmaster.repositories.impl;

import com.google.firebase.cloud.FirestoreClient;
import com.university.mcmaster.models.entities.User;
import com.university.mcmaster.repositories.UserRepo;
import com.university.mcmaster.repositories.utils.FirebaseUtils;
import com.university.mcmaster.utils.FirestoreConstants;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ExecutionException;

@Repository
public class UserRepoImpl extends FirebaseUtils<User> implements UserRepo {
    @Override
    public User findUserByEmail(String email) {
        return processQueryForEntity(
                FirestoreClient.getFirestore()
                        .collection(FirestoreConstants.FS_USERS)
                        .whereEqualTo("email",email).limit(1),
                User.class
        );
    }

    @Override
    public User findById(String id) {
        try {
            return FirestoreClient.getFirestore().collection(FirestoreConstants.FS_USERS).document(id).get().get().toObject(User.class);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
