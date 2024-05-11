package com.university.mcmaster.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum FilePurpose {
    rental_unit_image(false,false),
    rental_unit_poster_image(false,false),
    bank_statement(true,true),
    credit_score_report(true,true),
    gic_certificate(true,true),
    parents_bank_statement(true,true),
    student_id(true,true),
    national_id(true,true),
    user_profile_image(true,true),
    offered_lease_doc(true,true),
    signed_lease_doc(true,true);
    private FilePurpose(boolean deleteOld,boolean profileFile){
        this.deleteOld = deleteOld;
        this.profileFile = profileFile;
    }
    private boolean deleteOld;
    private boolean profileFile;

    public static boolean isValidFilePurpose(UserRole role,FilePurpose purpose){
        if(UserRole.student == role) return validForStudent().contains(purpose);
        if(UserRole.rental_unit_owner == role) return validForRentalUnitOwner().contains(purpose);
        return false;
    }

    public static List<FilePurpose> validForRentalUnitOwner(){
        return Arrays.asList(
                rental_unit_image,
                rental_unit_poster_image,
                user_profile_image,
                offered_lease_doc
        );
    }

    public static List<FilePurpose> validForStudent(){
        return Arrays.asList(bank_statement,
                credit_score_report,
                parents_bank_statement,
                gic_certificate,
                student_id,user_profile_image,
                signed_lease_doc,
                national_id);
    }
}
