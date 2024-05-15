package com.university.mcmaster.models.entities;

import com.university.mcmaster.enums.UserRole;
import com.university.mcmaster.enums.VerificationStatus;
import com.university.mcmaster.models.FirebaseCommonProps;
import com.university.mcmaster.utils.FirestoreConstants;
import lombok.*;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements FirebaseCommonProps {
        private static String collection = FirestoreConstants.FS_USERS;
        private String id;
        private VerificationStatus verificationStatus;
        private boolean deleted;
        private long verifiedOn;
        private long createdOn;
        private List<UserRole> role;

        private String email;
        private String phoneNumber;
        private String name;
        private String dob;
        private String nationality;
        private String emergencyContact;
        private String additionalEmail;
        private String occupation;
        private String preferredModOfContact;

        private String reason;

        private List<Address> addresses;
        private Map<String,StudentDocFile> documentPaths;
        private Map<String,Object> customFields;

        private StudentDocFile profileImage;

        private Dashboard dashboard;

        @Override
        public String getCollection() {
                return collection;
        }

        @Override
        public String getDbPath() {
                return null;
        }
}
