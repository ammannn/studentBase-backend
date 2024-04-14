package com.university.mcmaster.repositories;

import com.university.mcmaster.models.entities.LikeAndRating;

import java.util.HashMap;

public interface LikeAndRatingRepo {
    LikeAndRating findByUserIdAndRentalUnitIdAndDeletedFalse(String userId, String rentalUnitId);

    boolean save(LikeAndRating likeAndRating);

    void updateLikeAndRatingDoc(String id, HashMap<String, Object> liked);
}
