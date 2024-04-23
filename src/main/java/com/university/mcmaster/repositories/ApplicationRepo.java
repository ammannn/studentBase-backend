package com.university.mcmaster.repositories;

import com.university.mcmaster.enums.ApplicationStatus;
import com.university.mcmaster.models.entities.Application;

import java.util.List;
import java.util.Map;

public interface ApplicationRepo {
    boolean save(Application application);
    Application findById(String applicationId);
    List<Application> getApplicationByNullableRentalUnitIdAndStatusAndDeletedFalse(String rentalUnitId, ApplicationStatus status);
    List<Application> getPaginatedApplicationsByNullableRentalUnitIdAndStatusAndDeletedFalse(String rentalUnitId, ApplicationStatus status, int limit, String lastSeen);

    List<Application> getPaginatedApplicationsByStudentIdAndNullableRentalUnitIdAndStatusAndDeletedFalse(String userId, String rentalUnitId, ApplicationStatus status, int limit, String lastSeen);

    boolean update(String id, Map<String, Object> updateMap);
}
