package com.university.mcmaster.services.impl;

import com.university.mcmaster.enums.UserRole;
import com.university.mcmaster.exceptions.InvalidParamValueException;
import com.university.mcmaster.exceptions.UnAuthenticatedUserException;
import com.university.mcmaster.models.dtos.request.ApiResponse;
import com.university.mcmaster.models.dtos.request.RateRentalUnitRequestDto;
import com.university.mcmaster.models.dtos.response.RatingAndReviewResponse;
import com.university.mcmaster.models.dtos.response.RentalUnitForStudentForListing;
import com.university.mcmaster.models.entities.CustomUserDetails;
import com.university.mcmaster.models.entities.LikeAndRating;
import com.university.mcmaster.models.entities.RentalUnit;
import com.university.mcmaster.repositories.LikeAndRatingRepo;
import com.university.mcmaster.services.LikeAndRatingService;
import com.university.mcmaster.services.RentalUnitService;
import com.university.mcmaster.utils.ResponseMapper;
import com.university.mcmaster.utils.Utility;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LikeAndRatingServiceImpl implements LikeAndRatingService {

    private final LikeAndRatingRepo likeAndRatingRepo;
    private final RentalUnitService rentalUnitService;
    private final ResponseMapper responseMapper;

    @Override
    public ResponseEntity<?> likeRentalUnit(String rentalUnitId, boolean status, String requestId, HttpServletRequest request){
        CustomUserDetails userDetails = Utility.customUserDetails(request);
        if(null == userDetails || null == userDetails.getRoles() || false == userDetails.getRoles().contains(UserRole.student)) throw new UnAuthenticatedUserException();
        LikeAndRating likeAndRating = likeAndRatingRepo.findByUserIdAndRentalUnitIdAndDeletedFalse(userDetails.getId(),rentalUnitId);
        if(null == likeAndRating){
//            id , userId , rentalUnitId , createdOn
            likeAndRating = LikeAndRating.builder()
                    .id(UUID.randomUUID().toString())
                    .userId(userDetails.getId())
                    .rentalUnitId(rentalUnitId)
                    .liked(status)
                    .createdOn(Instant.now().toEpochMilli())
                    .build();
            boolean saved = likeAndRatingRepo.save(likeAndRating);
            if(saved && status){
                rentalUnitService.increamentLikeCountForRentalUnit(rentalUnitId);
            }
        }else{
            if(likeAndRating.isLiked() != status){
                likeAndRatingRepo.updateLikeAndRatingDoc(likeAndRating.getId(),new HashMap<String,Object>(){{
                    put("liked",status);
                }});
                if(status){
                    rentalUnitService.increamentLikeCountForRentalUnit(rentalUnitId);
                }else{
                    rentalUnitService.decreamentLikeCountForRentalUnit(rentalUnitId);
                }
            }
        }
        return ResponseEntity.status(200).body(ApiResponse.builder()
                        .msg("updated")
                .build());
    }

    @Override
    public ResponseEntity<?> rateRentalUnit(String rentalUnitId, int star, RateRentalUnitRequestDto requestDto, String requestId, HttpServletRequest request){
        CustomUserDetails userDetails = Utility.customUserDetails(request);
        if(null == userDetails || null == userDetails.getRoles() || false == userDetails.getRoles().contains(UserRole.student)) throw new UnAuthenticatedUserException();
        if(star <= 0 || star > 5) throw new InvalidParamValueException();
        LikeAndRating likeAndRating = likeAndRatingRepo.findByUserIdAndRentalUnitIdAndDeletedFalse(userDetails.getId(),rentalUnitId);
        if(null == likeAndRating){
            likeAndRating = LikeAndRating.builder()
                    .id(UUID.randomUUID().toString())
                    .userId(userDetails.getId())
                    .rentalUnitId(rentalUnitId)
                    .liked(false)
                    .rating(star)
                    .review(requestDto.getReview())
                    .createdOn(Instant.now().toEpochMilli())
                    .build();
            boolean saved = likeAndRatingRepo.save(likeAndRating);
            if(saved){
                new Thread(()->{
                    rentalUnitService.increamentRatingCountForRentalUnit(rentalUnitId,star);
                    rentalUnitService.decrementOrIncrementGeneralCountForRentalUnit(rentalUnitId,"reviews",1,"inc");
                }).start();
            }
        }else{
            HashMap<String,Object> updateMap = new HashMap<String,Object>();
            boolean updateCounts = false;
            if(likeAndRating.getRating() != star){
                updateMap.put("rating",star);
                updateCounts = true;
            }
            if(null != likeAndRating.getReview() && false == requestDto.getReview().equals(likeAndRating.getReview())){
                updateMap.put("review",likeAndRating.getReview());
            }
            likeAndRatingRepo.updateLikeAndRatingDoc(likeAndRating.getId(),updateMap);
            if(updateCounts) {
                rentalUnitService.increamentRatingCountForRentalUnit(rentalUnitId,star);
                if(likeAndRating.getRating() != 0) rentalUnitService.decreamentRatingCountForRentalUnit(rentalUnitId,likeAndRating.getRating());
            }
        }
        return ResponseEntity.status(200).body(ApiResponse.builder()
                        .msg("rating recorded")
                .build());
    }

    @Override
    public LikeAndRating getLikeAndRatingDocByUserIdAndRentalUnitId(String userId, String rentalUnitId) {
        return likeAndRatingRepo.findByUserIdAndRentalUnitIdAndDeletedFalse(userId,rentalUnitId);
    }

    @Override
    public ResponseEntity<?> getLikedRentalUnits(String requestId, HttpServletRequest request) {
        CustomUserDetails userDetails = Utility.customUserDetails(request);
        if(null == userDetails || null == userDetails.getRoles() || false == userDetails.getRoles().contains(UserRole.student)) throw new UnAuthenticatedUserException();
        List<LikeAndRating> likeAndRatings = likeAndRatingRepo.getLikeAndRatingDocsByUserIdAndDeletedFalse(userDetails.getId());
        List<RentalUnitForStudentForListing> res = likeAndRatings.stream().map(lar->{
            RentalUnit rentalUnit = rentalUnitService.getRentalUnitById(lar.getRentalUnitId());
            return responseMapper.mapRentalUnitToResponseDtoForStudent(rentalUnit,lar);
        }).toList();
        return ResponseEntity.ok(ApiResponse.builder()
                        .data(res)
                .build());
    }

    @Override
    public ResponseEntity<?> getReviewsByRentalUnitId(String rentalUnitId,String lastSeen,int limit, String requestId, HttpServletRequest request) {
        CustomUserDetails userDetails = Utility.customUserDetails(request);
        if(null == userDetails || null == userDetails.getRoles() ) throw new UnAuthenticatedUserException();
        List<LikeAndRating> likeAndRatings = likeAndRatingRepo.getLikeAndRatingDocsByRentalUnitIdIdAndDeletedFalse(rentalUnitId,lastSeen,limit);
        List<RatingAndReviewResponse> res = responseMapper.getRatingAndReview(likeAndRatings);
        return ResponseEntity.ok(ApiResponse.builder()
                        .data(res)
                .build());
    }
}
