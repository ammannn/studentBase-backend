package com.university.mcmaster.repositories;

import com.university.mcmaster.models.entities.AdminConfig;

import java.util.HashMap;

public interface AdminRepo {
    AdminConfig getAdminConfig();

    void saveAdminConfig(AdminConfig data);

    void updateAdminConfig(HashMap<String, Object> updateMap);
}
