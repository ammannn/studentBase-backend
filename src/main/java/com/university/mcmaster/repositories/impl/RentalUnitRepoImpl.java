package com.university.mcmaster.repositories.impl;

import com.google.cloud.firestore.Query;
import com.google.firebase.cloud.FirestoreClient;
import com.university.mcmaster.models.entities.RentalUnit;
import com.university.mcmaster.repositories.RentalUnitRepo;
import com.university.mcmaster.repositories.utils.FirebaseUtils;
import com.university.mcmaster.utils.FirestoreConstants;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Repository
public class RentalUnitRepoImpl extends FirebaseUtils<RentalUnit> implements RentalUnitRepo {

    @Override
    public List<RentalUnit> getRentalUnitByUserIdAndDeletedFalse(String userId, int limit, String lastSeen) {
        Query query = FirestoreClient.getFirestore()
                .collection(FirestoreConstants.FS_RENTAL_UNITS)
                .whereEqualTo("userId",userId)
                .whereEqualTo("deleted",false);
        query = addLastSeen(query,FirestoreConstants.FS_RENTAL_UNITS,lastSeen);
        if(limit > 0) query = query.limit(limit);
        return processQueryForEntityList(query,RentalUnit.class);
    }

    @Override
    public void update(String rentalUnitId, Map<String, Object> updateMap) {
        update(rentalUnitId,FirestoreConstants.FS_RENTAL_UNITS,updateMap);
    }

    @Override
    public RentalUnit findById(String rentalUnitId) {
        try {
            return FirestoreClient.getFirestore().collection(FirestoreConstants.FS_RENTAL_UNITS)
                    .document(rentalUnitId).get().get().toObject(RentalUnit.class);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
