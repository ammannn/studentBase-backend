package com.university.mcmaster.repositories;

import com.university.mcmaster.models.entities.VisitingSchedule;

import java.util.Map;

public interface CalendarRepo {
    boolean save(VisitingSchedule schedule);

    VisitingSchedule findByUserId(String ownerId);

    boolean update(String scheduleId,Map<String, Object> updateMap);
}
