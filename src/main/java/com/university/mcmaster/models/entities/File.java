package com.university.mcmaster.models.entities;

import com.university.mcmaster.enums.FilePurpose;
import com.university.mcmaster.models.FirebaseCommonProps;
import com.university.mcmaster.utils.FirestoreConstants;
import lombok.*;

@Getter@Setter@AllArgsConstructor
@NoArgsConstructor@Builder
public class File implements FirebaseCommonProps {
    public static String collection = FirestoreConstants.FS_FILES;
    private String id;
    private String userId;
    private long createdOn;
    private String filePath;
    private boolean deleted;
    private boolean uploadedOnGcp;
    private FilePurpose purpose;

    @Override
    public String getCollection() {
        return collection;
    }

    @Override
    public String getDbPath() {
        return null;
    }
}
