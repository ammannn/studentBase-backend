package com.university.mcmaster.integrations.sheerid.model;

import com.university.mcmaster.models.FirebaseCommonProps;
import com.university.mcmaster.utils.FirestoreConstants;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SheerIdVerificationDetails implements FirebaseCommonProps {
    private String id;
    private String programId;
    private String trackingId;
    private String personId;
    private long created;
    private long updated;
    private String verificationMethod;
    private SheerIdStudentVerificationResponse lastResponse;
    private SheerIdPersonInfo personInfo;
    private int docUploadRejectionCount;
    private List<List<String>> docUploadRejectionReasons;
    private List<SheerIdProgramSegment> confirmedSegments;
    private List<String> approvingVerificationTypes;
    private String deviceFingerprintHash;
    private String ipAddress;
    private String ipAddressExtended;

    @Override
    public String getCollection() {
        return FirestoreConstants.VERIFICATION_DATA;
    }

    @Override
    public String getDbPath() {
        return "";
    }
}
