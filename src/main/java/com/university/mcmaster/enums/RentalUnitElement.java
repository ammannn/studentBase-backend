package com.university.mcmaster.enums;

import lombok.Getter;

@Getter
public enum RentalUnitElement {
    living_room(10)
    ,bed_room(10)
    ,beds(10)
    ,dining_room(10)
    ,bath_room(10)
    ,baths(10)
    ,kitchen(10)
    ,others(10);

    private int allowedFiles;

    RentalUnitElement(int allowedFiles){
        this.allowedFiles = allowedFiles;
    }

    public static int getTotalAllowedFiles() {
        return living_room.getAllowedFiles() +
        bed_room.getAllowedFiles() +
        baths.getAllowedFiles() +
        beds.getAllowedFiles() +
        dining_room.getAllowedFiles() +
        bath_room.getAllowedFiles() +
        kitchen.getAllowedFiles() +
        others.getAllowedFiles() ;
    }
}
