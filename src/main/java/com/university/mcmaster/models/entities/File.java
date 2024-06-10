package com.university.mcmaster.models.entities;

import com.google.firebase.database.IgnoreExtraProperties;
import com.university.mcmaster.enums.FilePurpose;
import com.university.mcmaster.enums.RentalUnitElement;
import com.university.mcmaster.models.FirebaseCommonProps;
import com.university.mcmaster.utils.FirestoreConstants;
import lombok.*;

@Getter@Setter@AllArgsConstructor
@NoArgsConstructor@Builder
@IgnoreExtraProperties
public class File implements FirebaseCommonProps {
    public static String collection = FirestoreConstants.FS_FILES;
    private String id;
    private String userId;
    private long createdOn;
    private String filePath;
    private boolean deleted;
    private boolean uploadedOnGcp;
    private FilePurpose purpose;
    private String rentalUnitId;
    private String fileName;
    private RentalUnitElement rentalUnitElement;
    private String applicationId;

    @Override
    public String getCollection() {
        return collection;
    }

    @Override
    public String getDbPath() {
        return null;
    }
}
