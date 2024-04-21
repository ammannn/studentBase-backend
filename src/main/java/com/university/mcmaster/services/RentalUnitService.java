package com.university.mcmaster.services;

import com.university.mcmaster.enums.VerificationStatus;
import com.university.mcmaster.models.dtos.request.AddUpdateRentalUnitRequestDto;
import com.university.mcmaster.models.dtos.request.ApiResponse;
import com.university.mcmaster.models.dtos.response.RentalUnitForStudent;
import com.university.mcmaster.models.entities.LikeAndRating;
import com.university.mcmaster.models.entities.RentalUnit;
import com.university.mcmaster.utils.GcpStorageUtil;
import com.university.mcmaster.utils.Utility;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;

public interface RentalUnitService {

    static RentalUnitForStudent mapRentalUnitToResponseDtoForStudent(RentalUnit r, LikeAndRating likeAndRating) {
        boolean liked = false;
        double avgRating = Utility.getAverageRating(r.getRating());
        int likes = r.getLikes();
        int givenRating = 0;
        if(null != likeAndRating){
            liked = likeAndRating.isLiked();
            givenRating = likeAndRating.getRating();
        }
        return RentalUnitForStudent.builder()
                .rent(r.getRent())
                .deposit(r.getDeposit())
                .address(r.getAddress())
                .liked(liked)
                .avgRating(avgRating)
                .likes(likes)
                .contact(r.getContact())
                .givenRating(givenRating)
                .features(r.getFeatures())
                .rentalUnitStatus(r.getRentalUnitStatus())
                .posterImageUrl(null != r.getPosterImagePath() ? GcpStorageUtil.createGetUrl(r.getPosterImagePath()).toString() : null)
                .build();
    }
    
    ResponseEntity<ApiResponse<?>> getRentalUnits(int limit,String lastSeen,String requestId, HttpServletRequest request);
    ResponseEntity<ApiResponse<?>> addRentalUnit(AddUpdateRentalUnitRequestDto requestDto, String requestId, HttpServletRequest request);
    ResponseEntity<ApiResponse<?>> updateRentalUnits(String rentalUnitId,AddUpdateRentalUnitRequestDto requestDto,String requestId, HttpServletRequest request);
    ResponseEntity<ApiResponse<?>> deleteRentalUnits(String rentalUnitId, String requestId, HttpServletRequest request);
    void updateRentalUnitPosterImage(String rentalUnitId, String imageId,String imagePath);
    List<RentalUnit> getPaginatedRentalUnitsByVerificationStatusAndDeletedFalseForAdmin(VerificationStatus verificationStatus, int limit, String lastSeen);

    RentalUnit findRentalUnitById(String rentalUnitId);

    void updateRentalUnit(String rentalUnitId, HashMap<String, Object> hashMap);

    void increamentLikeCountForRentalUnit(String rentalUnitId);

    void decreamentLikeCountForRentalUnit(String rentalUnitId);

    void increamentRatingCountForRentalUnit(String rentalUnitId, int star);

    void decreamentRatingCountForRentalUnit(String rentalUnitId, int rating);

    RentalUnit getRentalUnitById(String rentalUnitId);

    ResponseEntity<ApiResponse<?>> getRentalUnitFeaturesStaticData(String requestId, HttpServletRequest request);
}
