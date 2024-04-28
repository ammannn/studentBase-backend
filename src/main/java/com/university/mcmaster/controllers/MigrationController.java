package com.university.mcmaster.controllers;

import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.firebase.cloud.FirestoreClient;
import com.university.mcmaster.enums.FilePurpose;
import com.university.mcmaster.models.entities.StudentDocFile;
import com.university.mcmaster.utils.FirestoreConstants;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/migration")
public class MigrationController {

    @GetMapping("/user-doc")
    public ResponseEntity<?> fixUserDocsMap(){
        try {
            for (QueryDocumentSnapshot document : FirestoreClient.getFirestore().collection(FirestoreConstants.FS_USERS)
                    .get().get().getDocuments()) {
                FirestoreClient.getFirestore().collection(FirestoreConstants.FS_USERS)
                        .document(document.getId())
                        .update(new HashMap<String,Object>(){{
                            for (FilePurpose filePurpose : FilePurpose.validForStudent()) {
                                put("documentPaths."+filePurpose.toString(), StudentDocFile.builder().build());
                            }
                        }});
            }
        } catch (InterruptedException | ExecutionException e) {
            return ResponseEntity.ok(e.getMessage());
        }
        return ResponseEntity.ok("updated docs map for users");
    }
}
