package com.university.mcmaster.repositories;

import com.university.mcmaster.models.entities.Application;

public interface ApplicationRepo {
    boolean save(Application application);
    Application findById(String applicationId);
}
