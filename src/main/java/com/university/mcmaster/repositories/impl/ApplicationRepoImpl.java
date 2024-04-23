package com.university.mcmaster.repositories.impl;

import com.google.cloud.firestore.Query;
import com.google.firebase.cloud.FirestoreClient;
import com.university.mcmaster.enums.ApplicationStatus;
import com.university.mcmaster.models.entities.Application;
import com.university.mcmaster.repositories.ApplicationRepo;
import com.university.mcmaster.repositories.utils.FirebaseUtils;
import com.university.mcmaster.utils.FirestoreConstants;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
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

    @Override
    public List<Application> getApplicationByNullableRentalUnitIdAndStatusAndDeletedFalse(String rentalUnitId, ApplicationStatus status) {
        return getPaginatedApplicationsByNullableRentalUnitIdAndStatusAndDeletedFalse(rentalUnitId,status,0,null);
    }

    @Override
    public List<Application> getPaginatedApplicationsByNullableRentalUnitIdAndStatusAndDeletedFalse(String rentalUnitId, ApplicationStatus status, int limit, String lastSeen) {
        Query query = FirestoreClient.getFirestore().collection(FirestoreConstants.FS_APPLICATIONS)
                .whereEqualTo("deleted",false);
        if(null != rentalUnitId && false == rentalUnitId.isEmpty()) query = query.whereEqualTo("rentalUnitId",rentalUnitId);
        if(null != status) query = query.whereEqualTo("applicationStatus",status);
        query = addLastSeen(query,FirestoreConstants.FS_APPLICATIONS,lastSeen);
        query = query.orderBy("lastUpdatedOn", Query.Direction.DESCENDING);
        if(limit > 0) query = query.limit(limit);
        return processQueryForEntityList(query,Application.class);
    }

    @Override
    public List<Application> getPaginatedApplicationsByStudentIdAndNullableRentalUnitIdAndStatusAndDeletedFalse(String userId, String rentalUnitId, ApplicationStatus status, int limit, String lastSeen) {
        Query query = FirestoreClient.getFirestore().collection(FirestoreConstants.FS_APPLICATIONS)
                .whereArrayContains("students",userId)
                .whereEqualTo("deleted",false);
        if(null != rentalUnitId && false == rentalUnitId.isEmpty()) query = query.whereEqualTo("rentalUnitId",rentalUnitId);
        if(null != status) query = query.whereEqualTo("applicationStatus",status);
        query = addLastSeen(query,FirestoreConstants.FS_APPLICATIONS,lastSeen);
        query = query.orderBy("lastUpdatedOn", Query.Direction.DESCENDING);
        if(limit > 0) query = query.limit(limit);
        return processQueryForEntityList(query,Application.class);
    }

    @Override
    public boolean update(String id, Map<String, Object> updateMap) {
        return update(id,FirestoreConstants.FS_APPLICATIONS,updateMap);
    }
}
