package com.university.mcmaster.models.entities;

import com.university.mcmaster.enums.UserRole;
import com.university.mcmaster.models.FirebaseCommonProps;
import com.university.mcmaster.utils.FirestoreConstants;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements FirebaseCommonProps {
        private static String collection = FirestoreConstants.FS_USERS;
        private String id;
        private String email;
        private String phoneNumber;
        private String name;
        private boolean verified;
        private boolean deleted;
        private long verifiedOn;
        private long createdOn;
        private List<UserRole> role;

        @Override
        public String getCollection() {
                return collection;
        }

        @Override
        public String getDbPath() {
                return null;
        }
}
