package com.university.mcmaster.repositories;

import com.university.mcmaster.models.entities.LikeAndRating;

import java.util.HashMap;
import java.util.List;

public interface LikeAndRatingRepo {
    LikeAndRating findByUserIdAndRentalUnitIdAndDeletedFalse(String userId, String rentalUnitId);

    boolean save(LikeAndRating likeAndRating);

    void updateLikeAndRatingDoc(String id, HashMap<String, Object> liked);

    List<LikeAndRating> getLikeAndRatingDocsByUserIdAndDeletedFalse(String userId);

    List<LikeAndRating> getLikeAndRatingDocsByRentalUnitIdIdAndDeletedFalse(String rentalUnitId, String lastSeen, int limit);
}
