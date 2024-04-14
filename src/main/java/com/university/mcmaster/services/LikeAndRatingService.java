package com.university.mcmaster.services;

import com.university.mcmaster.models.entities.LikeAndRating;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface LikeAndRatingService {
    ResponseEntity<?> likeRentalUnit(String rentalUnitId, boolean status, String requestId, HttpServletRequest request);

    ResponseEntity<?> rateRentalUnit(String rentalUnitId, int star, String requestId, HttpServletRequest request);

    LikeAndRating getLikeAndRatingDocByUserIdAndRentalUnitId(String id, String id1);
}
