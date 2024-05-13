package com.university.mcmaster.enums;

import java.util.Arrays;

public enum PaymentRequestType {
    payment_for_listing, payment_for_deposit;

    public static boolean isValid(PaymentRequestType requestType,UserRole userRole){
        if(UserRole.student == userRole) {
            return Arrays.asList(payment_for_deposit).contains(requestType);
        } else if(UserRole.rental_unit_owner == userRole) {
            return Arrays.asList(payment_for_listing).contains(requestType);
        }
        return false;
    }
}
