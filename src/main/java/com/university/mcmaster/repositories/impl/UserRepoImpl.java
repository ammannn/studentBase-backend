package com.university.mcmaster.repositories.impl;

import com.google.cloud.firestore.Query;
import com.google.firebase.cloud.FirestoreClient;
import com.university.mcmaster.enums.VerificationStatus;
import com.university.mcmaster.models.entities.User;
import com.university.mcmaster.repositories.UserRepo;
import com.university.mcmaster.repositories.utils.FirebaseUtils;
import com.university.mcmaster.utils.FirestoreConstants;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Queue;
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
            if(null == id || id.trim().isEmpty()) return null;
            return FirestoreClient.getFirestore().collection(FirestoreConstants.FS_USERS).document(id).get().get().toObject(User.class);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(String userId,Map<String, Object> updateMap) {
        update(userId,FirestoreConstants.FS_USERS,updateMap);
    }

    @Override
    public List<User> getPaginatedUsersByVerificationStatusForAdmin(VerificationStatus verificationStatus, int limit, String lastSeen) {
        Query query = FirestoreClient.getFirestore().collection(FirestoreConstants.FS_USERS)
                .whereEqualTo("verificationStatus",verificationStatus)
                .limit(limit);
        query = addLastSeen(query,FirestoreConstants.FS_USERS,lastSeen);
        return processQueryForEntityList(
                query,User.class
        );
    }
}
