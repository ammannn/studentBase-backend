package com.university.mcmaster.enums;

import lombok.Getter;

@Getter
public enum RentalUnitElement {
    living_room(2)
    ,bed_room(2)
    ,beds(2)
    ,dining_room(2)
    ,bath_room(2)
    ,baths(2)
    ,kitchen(2)
    ,others(5);

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
