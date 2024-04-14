package com.university.mcmaster.models.entities;

import com.university.mcmaster.models.FirebaseCommonProps;
import com.university.mcmaster.utils.FirestoreConstants;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikeAndRating implements FirebaseCommonProps {

    private static String collection = FirestoreConstants.FS_LIKE_AND_RATING;

    private String id;
    private String userId;
    private String rentalUnitId;
    private boolean deleted;
    private boolean liked;
    private int rating;
    private long createdOn;

    @Override
    public String getCollection() {
        return "";
    }

    @Override
    public String getDbPath() {
        return "";
    }
}
