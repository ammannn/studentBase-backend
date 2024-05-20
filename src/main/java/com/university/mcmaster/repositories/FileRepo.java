package com.university.mcmaster.repositories;

import com.university.mcmaster.enums.RentalUnitElement;
import com.university.mcmaster.models.entities.File;

import java.util.HashMap;
import java.util.List;

public interface FileRepo {
//    void save(File file);

    File findById(String fileId);

    void update(String fileId, HashMap<String, Object> uploadedOnGcp);

    boolean save(File file);

    List<File> getFilesByRentalUnitIdAndDeletedFalseAndUploadedOnGcpTrue(String rentalUnitId);

    List<File> getFilesByRentalUnitIdAndRentalUnitElementDeletedFalseAndUploadedOnGcpTrue(String rentalUnitId, RentalUnitElement element);

    List<File> getFilesByRentalUnitIdAndUploadedOnGcpTrueAndDeletedFalse(String id);

    List<File> getFilesByUserIdAndPurposeLeaseDocAndDeletedFalse(String userId);
}
