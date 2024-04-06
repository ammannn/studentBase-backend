package com.university.mcmaster.repositories;

import com.university.mcmaster.models.entities.RentalUnit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface RentalUnitRepo {
    boolean save(RentalUnit rentalUnit);

    List<RentalUnit> getRentalUnitByUserIdAndDeletedFalse(String id, int limit, String lastSeen);

    void update(String rentalUnitId, Map<String, Object> posterImageId);

    RentalUnit findById(String rentalUnitId);
}
