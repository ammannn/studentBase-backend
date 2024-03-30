package com.university.mcmaster.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.university.mcmaster.models.entities.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class FirebaseAuthenticationService {
    public static CustomUserDetails verifyToken(String tokenStr) {
        FirebaseToken token = null;
        try {
            token = FirebaseAuth.getInstance().verifyIdToken(tokenStr,true);
            Map<String,Object> claims = token.getClaims();
            return new CustomUserDetails(
                    token.getUid(),
                    claims.containsKey("roles") ? ((String) claims.get("roles")).split(",") : new String[]{},
                    token.getEmail(),
                    claims.containsKey("registered") ? (Boolean) claims.get("registered") : false
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean updateClaims(String userId, Map<String,Object> claims){
        UserRecord.UpdateRequest request = new UserRecord.UpdateRequest(userId);
        try {
//            request.setEmailVerified(true);
            request.setCustomClaims(claims);
            FirebaseAuth.getInstance().updateUser(request);
            return true;
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
            log.trace("failed to update claims for user : " + userId);
        }
        return false;
    }
}
