package com.university.mcmaster.models.entities;

import com.university.mcmaster.enums.ApplicationStatus;
import com.university.mcmaster.models.FirebaseCommonProps;
import com.university.mcmaster.utils.FirestoreConstants;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Application implements FirebaseCommonProps {
    private static String collection = FirestoreConstants.FS_APPLICATIONS;
    private String id;
    private String rentalUnitId;
    private String rentalUnitOwnerId;
    private List<String> students;
    private String createdBy;
    private ApplicationStatus applicationStatus;
    private long createdOn;
    private long lastUpdatedOn;
    private RequestedVisitingSchedule visitingSchedule;
    private boolean deleted;

    @Override
    public String getCollection() {
        return collection;
    }

    @Override
    public String getDbPath() {
        return "";
    }
}
