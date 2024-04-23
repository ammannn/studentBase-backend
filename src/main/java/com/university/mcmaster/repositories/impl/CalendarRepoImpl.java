package com.university.mcmaster.repositories.impl;

import com.google.firebase.cloud.FirestoreClient;
import com.university.mcmaster.models.entities.VisitingSchedule;
import com.university.mcmaster.repositories.CalendarRepo;
import com.university.mcmaster.repositories.utils.FirebaseUtils;
import com.university.mcmaster.utils.FirestoreConstants;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class CalendarRepoImpl extends FirebaseUtils<VisitingSchedule> implements CalendarRepo {


    @Override
    public VisitingSchedule findByUserId(String ownerId) {
        return processQueryForEntity(FirestoreClient.getFirestore().collection(FirestoreConstants.FS_VISITING_SCHEDULE)
                .whereEqualTo("userId",ownerId)
                .limit(1),VisitingSchedule.class);
    }

    @Override
    public boolean update(String scheduleId, Map<String, Object> updateMap) {
        return update(scheduleId,FirestoreConstants.FS_VISITING_SCHEDULE,updateMap);
    }
}
