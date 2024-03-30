package com.university.mcmaster.repositories;

import com.university.mcmaster.models.entities.File;

import java.util.HashMap;

public interface FileRepo {
//    void save(File file);

    File findById(String fileId);

    void update(String fileId, HashMap<String, Object> uploadedOnGcp);

    boolean save(File file);
}
