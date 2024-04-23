package com.university.mcmaster.models.entities;

import com.university.mcmaster.models.FirebaseCommonProps;
import com.university.mcmaster.utils.FirestoreConstants;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VisitingSchedule implements FirebaseCommonProps {
    public static String collection = FirestoreConstants.FS_VISITING_SCHEDULE;
    private String id;
    private String userId;
    private long createdOn;
    private boolean deleted;
    private List<Day> days;
    private String timeZone;

    @Override
    public String getCollection() {
        return this.collection;
    }

    @Override
    public String getDbPath() {
        return "";
    }
}
