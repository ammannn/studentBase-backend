package com.university.mcmaster.enums;

import java.util.Arrays;

public enum ApplicationStatus {
    tour_requested,
    view_property,
    pending_document_upload,
    review_in_process,
    approved,
    rejected;
//
//    public boolean validStageRequest(ApplicationStatus status,UserRole role){
//        if(UserRole.student == role){
//            return Arrays.asList(tour_requested,view_property).contains(status);
//        }
//    }
}