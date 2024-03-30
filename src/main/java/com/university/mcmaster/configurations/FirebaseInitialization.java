package com.university.mcmaster.configurations;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.university.mcmaster.utils.EnvironmentVariables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.util.HashMap;

@Configuration
@Slf4j
public class FirebaseInitialization {

    @PostConstruct
    public void init(){
        System.out.println("***************************************");
        try {
            FileInputStream serviceAccount = new FileInputStream(EnvironmentVariables.GOOGLE_APPLICATION_CREDENTIALS);
            GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(credentials)
                    .build();
            FirebaseApp.initializeApp(options);
            log.trace("initialized firebase project :)");
        }catch (Exception e){
            log.trace("failed to initialize google services for project, error : " + e.getMessage());
        }
    }
}
