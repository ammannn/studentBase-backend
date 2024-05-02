package com.university.mcmaster.utils;

import com.university.mcmaster.enums.RentalUnitElement;
import com.university.mcmaster.models.dtos.response.*;
import com.university.mcmaster.models.entities.*;
import com.university.mcmaster.repositories.LikeAndRatingRepo;
import com.university.mcmaster.repositories.RentalUnitRepo;
import com.university.mcmaster.repositories.UserRepo;
import com.university.mcmaster.services.FileService;
import com.university.mcmaster.services.impl.LikeAndRatingServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ResponseMapper {

    @Autowired
    @Lazy
    private FileService fileService;
    @Autowired
    @Lazy
    private RentalUnitRepo rentalUnitRepo;
    @Autowired
    @Lazy
    private UserRepo userRepo;
    @Autowired
    @Lazy
    private LikeAndRatingRepo likeAndRatingRepo;

    public RentalUnitForStudentForListing mapRentalUnitToResponseDtoForStudent(RentalUnit r, LikeAndRating likeAndRating) {
        boolean liked = false;
        double avgRating = Utility.getAverageRating(r.getRating());
        int likes = r.getLikes();
        int givenRating = 0;
        if (null != likeAndRating) {
            liked = likeAndRating.isLiked();
            givenRating = likeAndRating.getRating();
        }
        return RentalUnitForStudentForListing.builder()
                .rent(r.getRent())
                .deposit(r.getDeposit())
                .address(r.getAddress())
                .liked(liked)
                .avgRating(avgRating)
                .likes(likes)
                .contact(r.getContact())
                .givenRating(givenRating)
                .images(getRentalUnitImages(r.getId()))
                .features(r.getFeatures())
                .rentalUnitStatus(r.getRentalUnitStatus())
                .posterImageUrl(null != r.getPosterImagePath() ? GcpStorageUtil.createGetUrl(r.getPosterImagePath()).toString() : null)
                .build();
    }

    public List<ApplicationForRentalUnitOwner> getApplicationsForRentalUnitOwner(List<Application> applications, String requestId) {
        List<ApplicationForRentalUnitOwner> res = new ArrayList<>();
        Map<String, RentalUnitForOwner> rentalUnitMap = new HashMap<>();
        Map<String, StudentForOwner> userMap = new HashMap<>();
        for (Application application : applications) {
            res.add(getApplicationForRentalUnitOwner(application,rentalUnitMap,userMap,requestId));
        }
        return res;
    }

    public ApplicationForRentalUnitOwner getApplicationForRentalUnitOwner(Application application, Map<String, RentalUnitForOwner> rentalUnitMap, Map<String, StudentForOwner> userMap, String requestId) {
        return ApplicationForRentalUnitOwner.builder()
                .applicationId(application.getId())
                .rentalUnit(getRentalUnitByIdForOwner(application.getRentalUnitId(), rentalUnitMap))
                .applicationStatus(application.getApplicationStatus())
                .lastUpdatedOn(application.getLastUpdatedOn())
                .createdOn(application.getCreatedOn())
                .students(application.getStudents().stream().map(s -> getStudentByIdForRentalUnitOwner(s, userMap)).collect(Collectors.toList()))
                .createdBy(getStudentByIdForRentalUnitOwner(application.getCreatedBy(), userMap))
                .visitingSchedule(application.getVisitingSchedule())
                .build();
    }

    private StudentForOwner getStudentByIdForRentalUnitOwner(String userId, Map<String, StudentForOwner> userMap) {
        if (userMap.containsKey(userId)) return userMap.get(userId);
        User user = userRepo.findById(userId);
        StudentForOwner studentForOwner = getStudentForRentalUnitOwner(user);
        userMap.put(userId, studentForOwner);
        return studentForOwner;
    }

    public StudentForStudent getStudentByIdForStudent(String userId, Map<String, StudentForStudent> userMap) {
        if (userMap.containsKey(userId)) return userMap.get(userId);
        User user = userRepo.findById(userId);
        StudentForStudent studentForOwner = getStudentForStudent(user);
        userMap.put(userId, studentForOwner);
        return studentForOwner;
    }

    private StudentForOwner getStudentForRentalUnitOwner(User user) {
        return StudentForOwner.builder()
                .name(user.getName())
                .email(user.getEmail())
                .nationality(user.getNationality())
                .verificationStatus(user.getVerificationStatus())
                .verifiedOn(user.getVerifiedOn())
                .phoneNumber(user.getPhoneNumber())
                .userId(user.getId())
                .docs(getStudentDocs(user))
                .build();
    }

    public StudentForStudent getStudentForStudent(User user) {
        return StudentForStudent.builder()
                .name(user.getName())
                .email(user.getEmail())
                .nationality(user.getNationality())
                .verificationStatus(user.getVerificationStatus())
                .verifiedOn(user.getVerifiedOn())
                .phoneNumber(user.getPhoneNumber())
                .userId(user.getId())
                .profileImageUrl((null != user.getProfileImage() && null != user.getProfileImage().getPath() && false == user.getProfileImage().getPath().trim().isEmpty()) ? GcpStorageUtil.createGetUrl(user.getProfileImage().getPath()).toString() : "")
                .docs(getStudentDocs(user))
                .build();
    }

    private RentalUnitForOwner getRentalUnitByIdForOwner(String rentalUnitId, Map<String, RentalUnitForOwner> cache) {
        if (cache.containsKey(rentalUnitId)) return cache.get(rentalUnitId);
        RentalUnit rentalUnit = rentalUnitRepo.findById(rentalUnitId);
        RentalUnitForOwner res = getRentalUnitForOwner(rentalUnit);
        cache.put(rentalUnitId, res);
        return res;
    }

    public List<RentalUnitForOwner> getRentalUnitsForOwner(List<RentalUnit> rentalUnits) {
        List<RentalUnitForOwner> res = new ArrayList<>();
        for (RentalUnit rentalUnit : rentalUnits) {
            res.add(getRentalUnitForOwner(rentalUnit));
        }
        return res;
    }

    public RentalUnitForOwner getRentalUnitForOwner(RentalUnit rentalUnit) {
        String url = null;
        if (null != rentalUnit.getPosterImagePath())
            url = GcpStorageUtil.createGetUrl(rentalUnit.getPosterImagePath()).toString();
        return RentalUnitForOwner.builder()
                .avgRating(Utility.getAverageRating(rentalUnit.getRating()))
                .likes(rentalUnit.getLikes())
                .rentalUnitStatus(rentalUnit.getRentalUnitStatus())
                .rentalUnitId(rentalUnit.getId())
                .rent(rentalUnit.getRent())
                .deposit(rentalUnit.getDeposit())
                .verificationStatus(rentalUnit.getVerificationStatus())
                .address(rentalUnit.getAddress())
                .contact(rentalUnit.getContact())
                .features(rentalUnit.getFeatures())
                .rentalUnitStatus(rentalUnit.getRentalUnitStatus())
                .createdOn(rentalUnit.getCreatedOn())
                .images(getRentalUnitImages(rentalUnit.getId()))
                .posterImageUrl(url)
                .build();
    }

    public Map<String, HashMap<String, Object>> getStudentDocs(User user) {
        Map<String, HashMap<String, Object>> res = new HashMap<>();
        if (null != user && null != user.getDocumentPaths()) {
            for (Map.Entry<String, StudentDocFile> docEntry : user.getDocumentPaths().entrySet()) {
                res.put(docEntry.getKey(), new HashMap<String, Object>() {{
                    put("url", (null != docEntry.getValue() && null != docEntry.getValue().getPath() && false == docEntry.getValue().getPath().trim().isEmpty()) ? GcpStorageUtil.createGetUrl(docEntry.getValue().getPath()).toString() : "");
                    put("name", (null != docEntry.getValue()) ? docEntry.getValue().getName() : "");
                }});
            }
        }
        return res;
    }

    public List<ApplicationForStudent> getApplicationsForStudent(String userId, List<Application> applications, String requestId) {
        List<ApplicationForStudent> res = new ArrayList<>();
        Map<String, RentalUnitForStudentForListing> rentalUnitMap = new HashMap<>();
        Map<String, StudentForStudent> userMap = new HashMap<>();
        for (Application application : applications) {
            res.add(getApplicationForStudent(application,userId,requestId,userMap,rentalUnitMap));
        }
        return res;
    }

    public ApplicationForStudent getApplicationForStudent(Application application, String userId, String requestId, Map<String, StudentForStudent> userMap, Map<String, RentalUnitForStudentForListing> rentalUnitMap) {
        return ApplicationForStudent.builder()
                .applicationId(application.getId())
                .rentalUnit(getRentalUnitByIdForStudent(userId, application.getRentalUnitId(), rentalUnitMap))
                .applicationStatus(application.getApplicationStatus())
                .lastUpdatedOn(application.getLastUpdatedOn())
                .createdOn(application.getCreatedOn())
                .students(application.getStudents().stream().map(s -> getStudentByIdForStudent(s, userMap)).collect(Collectors.toList()))
                .createdBy(getStudentByIdForStudent(application.getCreatedBy(), userMap))
                .visitingSchedule(application.getVisitingSchedule())
                .build();
    }

    private RentalUnitForStudentForListing getRentalUnitByIdForStudent(String userId, String rentalUnitId, Map<String, RentalUnitForStudentForListing> cache) {
        if (cache.containsKey(rentalUnitId)) return cache.get(rentalUnitId);
        RentalUnit rentalUnit = rentalUnitRepo.findById(rentalUnitId);
        RentalUnitForStudentForListing res = mapRentalUnitToResponseDtoForStudent(rentalUnit, likeAndRatingRepo.findByUserIdAndRentalUnitIdAndDeletedFalse(userId, rentalUnitId));
        cache.put(rentalUnitId, res);
        return res;
    }

    public void setRentalUnitImages(RentalUnit rentalUnit) {
        if (null == rentalUnit.getCustomFields()) rentalUnit.setCustomFields(new HashMap<>());
        if (null != rentalUnit.getPosterImagePath())
            rentalUnit.getCustomFields().put("posterImagePath", GcpStorageUtil.createGetUrl(rentalUnit.getPosterImagePath()));
        rentalUnit.getCustomFields().put("images",getRentalUnitImages(rentalUnit.getId()));
    }

    public Map<String,List<HashMap<String,Object>>> getRentalUnitImages(String rentalUnitId){
        return getRentalUnitImages(fileService.getFilesByRentalUnitIdAndUploadedOnGcpTrueAndDeletedFalse(rentalUnitId));
    }

    public Map<String,List<HashMap<String,Object>>> getRentalUnitImages(List<File> files){
        return files.stream().collect(Collectors.groupingBy(file -> {
            if (null != file.getRentalUnitElement()) return file.getRentalUnitElement().toString();
            return RentalUnitElement.others.toString();
        }, Collectors.collectingAndThen(Collectors.toList(), l -> l.stream().map(f -> {
            return new HashMap<String, Object>() {{
                put("imageId", f.getId());
                put("url", GcpStorageUtil.createGetUrl(f.getFilePath()));
            }};
        }).collect(Collectors.toList()))));
    }
}
