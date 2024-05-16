package com.university.mcmaster.repositories.impl;

import com.google.cloud.firestore.Query;
import com.google.firebase.cloud.FirestoreClient;
import com.university.mcmaster.enums.RentalUnitStatus;
import com.university.mcmaster.enums.VerificationStatus;
import com.university.mcmaster.models.dtos.request.SearchRentalUnitRequestDto;
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

    @Override
    public List<RentalUnit> getPaginatedRentalUnitsByVerificationStatusAndDeletedFalseForAdmin(VerificationStatus verificationStatus, int limit, String lastSeen) {
        Query query = FirestoreClient.getFirestore()
                .collection(FirestoreConstants.FS_RENTAL_UNITS)
                .whereEqualTo("verificationStatus",verificationStatus)
                .whereEqualTo("deleted",false);
        if(limit > 0 ) query = query.limit(limit);
        query = addLastSeen(query,FirestoreConstants.FS_RENTAL_UNITS,lastSeen);
        return processQueryForEntityList(query,RentalUnit.class);
    }

    @Override
    public List<RentalUnit> getPaginatedRentalUnitsByEligibilityTrueAndDeletedFalseAndSearchFilters(
            List<String> rentalUnitFeatureList, String country, String state, String city, long maxRent, long minRent, int limit, String lastSeen
    ) {
        Query query = FirestoreClient.getFirestore()
                .collection(FirestoreConstants.FS_RENTAL_UNITS)
                .whereEqualTo("eligibleForListing",true)
                .whereEqualTo("deleted",false)
                .whereEqualTo("address.country.value",country)
                .orderBy("lastUpdatedOn", Query.Direction.DESCENDING);
        if(null != state && false == state.trim().isEmpty()) query = query.whereEqualTo("address.state.value",state.trim());
        if(null != city && false == city.trim().isEmpty()) query = query.whereEqualTo("address.city",city.trim().toLowerCase());
        if(minRent > 0) query = query.whereGreaterThanOrEqualTo("rent",minRent);
        if(maxRent > 0)query = query.whereLessThanOrEqualTo("rent",maxRent);
        if(null != rentalUnitFeatureList && false == rentalUnitFeatureList.isEmpty()) query = query.whereArrayContainsAny("featureSearchList",rentalUnitFeatureList);
        if(limit > 0 ) query = query.limit(limit);
        query = addLastSeen(query,FirestoreConstants.FS_RENTAL_UNITS,lastSeen);
        return processQueryForEntityList(query,RentalUnit.class);
    }

    @Override
    public List<RentalUnit> getRentalUnitByUserIdAndDeletedFalseAndEligibilityForListing(String userId, boolean fetchLiveOnly, int limit, String lastSeen) {
        Query query = FirestoreClient.getFirestore()
                .collection(FirestoreConstants.FS_RENTAL_UNITS)
                .whereEqualTo("deleted",false);
        if(fetchLiveOnly) query = query.whereEqualTo("eligibleForListing",fetchLiveOnly);
        query = query.orderBy("lastUpdatedOn", Query.Direction.DESCENDING);
        if(limit > 0 ) query = query.limit(limit);
        query = addLastSeen(query,FirestoreConstants.FS_RENTAL_UNITS,lastSeen);
        return processQueryForEntityList(query,RentalUnit.class);
    }
}
