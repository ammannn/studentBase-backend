package com.university.mcmaster.repositories.impl;

import com.google.cloud.firestore.Query;
import com.google.firebase.cloud.FirestoreClient;
import com.university.mcmaster.models.entities.LikeAndRating;
import com.university.mcmaster.repositories.LikeAndRatingRepo;
import com.university.mcmaster.repositories.utils.FirebaseUtils;
import com.university.mcmaster.utils.FirestoreConstants;
import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class LikeAndRatingRepoImpl extends FirebaseUtils<LikeAndRating> implements LikeAndRatingRepo {
    @Override
    public LikeAndRating findByUserIdAndRentalUnitIdAndDeletedFalse(String userId, String rentalUnitId) {
        return processQueryForEntity(
                FirestoreClient.getFirestore().collection(FirestoreConstants.FS_LIKE_AND_RATING)
                        .whereEqualTo("userId",userId)
                        .whereEqualTo("rentalUnitId",rentalUnitId)
                        .orderBy("createdOn", Query.Direction.DESCENDING)
                        .limit(1)
                        .whereEqualTo("deleted",false),LikeAndRating.class

        );
    }

    @Override
    public void updateLikeAndRatingDoc(String docId, HashMap<String, Object> updateMap) {
        update(docId,FirestoreConstants.FS_LIKE_AND_RATING,updateMap);
    }
}
