package com.university.mcmaster.utils;

import com.university.mcmaster.enums.ApplicationStatus;
import com.university.mcmaster.enums.RentalUnitElement;
import com.university.mcmaster.enums.RentalUnitStage;
import com.university.mcmaster.enums.VerificationStatus;
import com.university.mcmaster.models.dtos.response.*;
import com.university.mcmaster.models.entities.*;
import com.university.mcmaster.repositories.*;
import com.university.mcmaster.services.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ResponseMapper {

    private static final Logger log = LoggerFactory.getLogger(ResponseMapper.class);
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
    @Autowired
    private CalendarRepo calendarRepo;
    @Autowired
    private ApplicationRepo applicationRepo;

    public RentalUnitForStudentForListing mapRentalUnitToResponseDtoForStudent(String studentId,RentalUnit r) {
        boolean liked = false;
        double avgRating = Utility.getAverageRating(r.getRating());
        int likes = null != r.getCounts() && null != r.getCounts().get(Constants.CountNames.likes.toString()) ? r.getCounts().get(Constants.CountNames.likes.toString()) : 0;
        int reviews = null != r.getCounts() && null != r.getCounts().get(Constants.CountNames.reviews.toString()) ? r.getCounts().get(Constants.CountNames.reviews.toString()) : 0;
        int givenRating = 0;
        String givenReview = null;
        VisitingSchedule schedule =  calendarRepo.findByUserId(r.getUserId());
        LikeAndRating likeAndRating = likeAndRatingRepo.findByUserIdAndRentalUnitIdAndDeletedFalse(studentId,r.getId());
        if (null != likeAndRating) {
            liked = likeAndRating.isLiked();
            givenRating = likeAndRating.getRating();
            givenReview = likeAndRating.getReview();
        }
        Application application = applicationRepo.getApplicationByStudentIdAndRentalUnitIdAndDeletedFalse(studentId,r.getId());
        ApplicationForStudent applicationForStudent = null;
        if(null != application){
            applicationForStudent = getApplicationForStudent(application,studentId,new HashMap<>(),new HashMap<>(),false);
        }
        return RentalUnitForStudentForListing.builder()
                .rentalUnitId(r.getId())
                .rent(r.getRent())
                .deposit(r.getDeposit())
                .address(r.getAddress())
                .liked(liked)
                .avgRating(avgRating)
                .likes(likes)
                .reviews(reviews)
                .organizationName(r.getOrganizationName())
                .review(givenReview)
                .contact(r.getContact())
                .givenRating(givenRating)
                .images(getRentalUnitImages(r.getId()))
                .features(r.getFeatures())
                .bedsRemaining(Math.max(r.getRemainingBeds(), 0))
                .rentalUnitStatus(r.getRentalUnitStatus())
                .title(r.getTitle())
                .description(r.getDescription())
                .leaseTerm(r.getLeaseTerm())
                .leaseStartDate(r.getLeaseStartDate())
                .visitingSchedule(null  != schedule ? VisitingScheduleOfRentalUnitOwner.builder()
                        .days(schedule.getDays())
                        .timeZone(schedule.getTimeZone())
                        .build(): null)
                .posterImageUrl(null != r.getPosterImagePath() ? GcpStorageUtil.createGetUrl(r.getPosterImagePath()).toString() : null)
                .application(applicationForStudent)
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
        if(null != application.getOfferedLeaseDetails()){
            if(null != application.getOfferedLeaseDetails().getFilePath()){
                application.getOfferedLeaseDetails().setFilePath(GcpStorageUtil.createGetUrl(application.getOfferedLeaseDetails().getFilePath()).toString());
            }
        }
        if(null != application.getSignedLeaseDetails()){
            if(null != application.getSignedLeaseDetails().getFilePath()){
                application.getSignedLeaseDetails().setFilePath(GcpStorageUtil.createGetUrl(application.getSignedLeaseDetails().getFilePath()).toString());
            }
        }
        List<StudentForOwner> students = new ArrayList<>();
        boolean documentationCompleted = true;
        for (String studentId : application.getStudents()) {
            StudentForOwner temp = getStudentByIdForRentalUnitOwner(studentId, userMap);
            students.add(temp);
            documentationCompleted = documentationCompleted && temp.isDocumentationCompleted();
        }
        return ApplicationForRentalUnitOwner.builder()
                .applicationId(application.getId())
                .rentalUnit(getRentalUnitByIdForOwner(application.getRentalUnitId(), rentalUnitMap))
                .applicationStatus(application.getApplicationStatus())
                .lastUpdatedOn(application.getLastUpdatedOn())
                .createdOn(application.getCreatedOn())
                .students(students)
                .documentationCompleted(documentationCompleted)
                .createdBy(getStudentByIdForRentalUnitOwner(application.getCreatedBy(), userMap))
                .visitingSchedule(application.getVisitingSchedule())
                .offeredLeaseDetails(application.getOfferedLeaseDetails())
                .signedLeaseDetails(application.getSignedLeaseDetails())
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
        MethodResponse<Map<String, HashMap<String, Object>>,Boolean,?> docs = getStudentDocs(user);
        return StudentForOwner.builder()
                .name(user.getName())
                .email(user.getEmail())
                .nationality(user.getNationality())
                .verificationStatus(user.getVerificationStatus())
                .verifiedOn(user.getVerifiedOn())
                .phoneNumber(user.getPhoneNumber())
                .userId(user.getId())
                .docs(docs.getResult_1())
                .documentationCompleted(docs.getResult_2())
                .build();
    }

    public StudentForStudent getStudentForStudent(User user) {
        MethodResponse<Map<String, HashMap<String, Object>>,Boolean,?> docs = getStudentDocs(user);
        return StudentForStudent.builder()
                .name(user.getName())
                .email(user.getEmail())
                .nationality(user.getNationality())
                .verificationStatus(user.getVerificationStatus())
                .verifiedOn(user.getVerifiedOn())
                .phoneNumber(user.getPhoneNumber())
                .userId(user.getId())
                .profileImageUrl((null != user.getProfileImage() && null != user.getProfileImage().getPath() && false == user.getProfileImage().getPath().trim().isEmpty()) ? GcpStorageUtil.createGetUrl(user.getProfileImage().getPath()).toString() : "")
                .docs(docs.getResult_1())
                .documentationCompleted(docs.getResult_2())
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
        int likes = null != rentalUnit.getCounts() && null != rentalUnit.getCounts().get(Constants.CountNames.likes.toString()) ? rentalUnit.getCounts().get(Constants.CountNames.likes.toString()) : 0;
        int reviews = null != rentalUnit.getCounts() && null != rentalUnit.getCounts().get(Constants.CountNames.reviews.toString()) ? rentalUnit.getCounts().get(Constants.CountNames.reviews.toString()) : 0;
        return RentalUnitForOwner.builder()
                .avgRating(Utility.getAverageRating(rentalUnit.getRating()))
                .likes(likes)
                .reviews(reviews)
                .organizationName(rentalUnit.getOrganizationName())
                .sheerIdOrganizationId(rentalUnit.getSheerIdOrganizationId())
                .stage(getRentalUnitStage(rentalUnit))
                .bedsRemaining(Math.max(rentalUnit.getRemainingBeds(),0))
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
                .title(rentalUnit.getTitle())
                .leaseTerm(rentalUnit.getLeaseTerm())
                .counts(rentalUnit.getCounts())
                .leaseStartDate(rentalUnit.getLeaseStartDate())
                .description(rentalUnit.getDescription())
                .live(rentalUnit.isEligibleForListing())
                .build();
    }

    private RentalUnitStage getRentalUnitStage(RentalUnit rentalUnit) {
//     deposit_received,lease_signed, lease_offered , paperwork_in_review , viewing_booked , listing_approved
        if(rentalUnit.getCounts().getOrDefault(ApplicationStatus.payment_done.toString(),0) > 0){
            return RentalUnitStage.deposit_received;
        }
        if(rentalUnit.getCounts().getOrDefault(ApplicationStatus.lease_signed.toString(),0) > 0){
            return RentalUnitStage.lease_signed;
        }
        if(rentalUnit.getCounts().getOrDefault(ApplicationStatus.lease_offered.toString(),0) > 0){
            return RentalUnitStage.lease_offered;
        }
        if(rentalUnit.getCounts().getOrDefault(ApplicationStatus.review_in_process.toString(),0) > 0){
            return RentalUnitStage.paperwork_in_review;
        }
        if(rentalUnit.getCounts().getOrDefault(ApplicationStatus.visit_requested.toString(),0) > 0){
            return RentalUnitStage.viewing_booked;
        }
        if(VerificationStatus.verified == rentalUnit.getVerificationStatus()) return RentalUnitStage.listing_approved;
        if(VerificationStatus.failed == rentalUnit.getVerificationStatus()) return RentalUnitStage.listing_approval_failed;
        return RentalUnitStage.listing_approval_in_process;
    }

    public MethodResponse< Map<String, HashMap<String, Object>>,Boolean,?> getStudentDocs(User user) {
        MethodResponse< Map<String, HashMap<String, Object>>,Boolean,?> res = new MethodResponse<>();
        Map<String, HashMap<String, Object>> data = new HashMap<>();
        boolean documentationStatus = true;
        if (null != user && null != user.getDocumentPaths()) {
            for (Map.Entry<String, StudentDocFile> docEntry : user.getDocumentPaths().entrySet()) {
                boolean isAvailable = (null != docEntry.getValue() && null != docEntry.getValue().getPath() && false == docEntry.getValue().getPath().trim().isEmpty());
                documentationStatus = documentationStatus && isAvailable;
                data.put(docEntry.getKey(), new HashMap<String, Object>() {{
                    put("url", isAvailable ? GcpStorageUtil.createGetUrl(docEntry.getValue().getPath()).toString() : "");
                    put("name", (null != docEntry.getValue()) ? docEntry.getValue().getName() : "");
                }});
            }
        }
        res.setResult_1(data);
        res.setResult_2(documentationStatus);
        return res;
    }

    public List<ApplicationForStudent> getApplicationsForStudent(String userId, List<Application> applications) {
        List<ApplicationForStudent> res = new ArrayList<>();
        Map<String, RentalUnitForStudentForListing> rentalUnitMap = new HashMap<>();
        Map<String, StudentForStudent> userMap = new HashMap<>();
        for (Application application : applications) {
            res.add(getApplicationForStudent(application,userId,userMap,rentalUnitMap));
        }
        return res;
    }

    public ApplicationForStudent getApplicationForStudent(Application application, String userId, Map<String, StudentForStudent> userMap,
                                                          Map<String, RentalUnitForStudentForListing> rentalUnitMap) {

        return getApplicationForStudent(application,userId,userMap,rentalUnitMap,true);
    }

    public ApplicationForStudent getApplicationForStudent(Application application, String userId,
                                                          Map<String, StudentForStudent> userMap,
                                                          Map<String, RentalUnitForStudentForListing> rentalUnitMap,boolean setRentalUnit) {
        if(null != application.getOfferedLeaseDetails()){
            if(null != application.getOfferedLeaseDetails().getFilePath()){
                log.trace("setting offered lease doc for application : " + application.getId());
                application.getOfferedLeaseDetails().setFilePath(GcpStorageUtil.createGetUrl(application.getOfferedLeaseDetails().getFilePath()).toString());
            }else{
                log.trace("no lease doc path found for application : " + application.getId());
            }
        }else{
            log.trace("no lease doc found for application : " + application.getId());
        }
        if(null != application.getSignedLeaseDetails()){
            if(null != application.getSignedLeaseDetails().getFilePath()){
                log.trace("setting signed lease doc for application : " + application.getId());
                application.getSignedLeaseDetails().setFilePath(GcpStorageUtil.createGetUrl(application.getSignedLeaseDetails().getFilePath()).toString());
            }else{
                log.trace("no signed lease doc path found for application : " + application.getId());
            }
        }else{
            log.trace("no signed lease doc found for application : " + application.getId());
        }
        List<StudentForStudent> students = new ArrayList<>();
        boolean documentationCompleted = true;
        for (String studentId : application.getStudents()) {
            StudentForStudent temp = getStudentByIdForStudent(studentId, userMap);
            students.add(temp);
            documentationCompleted = documentationCompleted && temp.isDocumentationCompleted();
        }
        return ApplicationForStudent.builder()
                .applicationId(application.getId())
                .rentalUnit(setRentalUnit ? getRentalUnitByIdForStudent(userId, application.getRentalUnitId(), rentalUnitMap) : null)
                .applicationStatus(application.getApplicationStatus())
                .lastUpdatedOn(application.getLastUpdatedOn())
                .createdOn(application.getCreatedOn())
                .students(students)
                .documentationCompleted(documentationCompleted)
                .createdBy(getStudentByIdForStudent(application.getCreatedBy(), userMap))
                .visitingSchedule(application.getVisitingSchedule())
                .offeredLeaseDetails(application.getOfferedLeaseDetails())
                .signedLeaseDetails(application.getSignedLeaseDetails())
                .build();
    }

    private RentalUnitForStudentForListing getRentalUnitByIdForStudent(String userId, String rentalUnitId, Map<String, RentalUnitForStudentForListing> cache) {
        if (cache.containsKey(rentalUnitId)) return cache.get(rentalUnitId);
        RentalUnit rentalUnit = rentalUnitRepo.findById(rentalUnitId);
        RentalUnitForStudentForListing res = mapRentalUnitToResponseDtoForStudent(userId,rentalUnit);
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

    public List<RatingAndReviewResponse> getRatingAndReview(List<LikeAndRating> likeAndRatings) {
        return likeAndRatings.stream().map(lr->{
            User user = userRepo.findById(lr.getUserId());
            return RatingAndReviewResponse.builder()
                    .name(user.getName())
                    .rating(lr.getRating())
                    .review(lr.getReview())
                    .build();
        }).collect(Collectors.toList());
    }

    public int getTotalBeds(RentalUnitFeatures features) {
        return null != features && null != features.getFeaturesNumbers() ?  features.getFeaturesNumbers().getOrDefault("beds",0) : 0;
    }
}
