package com.wasin.wasin._core.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.wasin.wasin._core.exception.BaseException;
import com.wasin.wasin._core.exception.error.ServerException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;

@Profile({"dev", "prod"})
@Slf4j
@Configuration
public class FirebaseConfig {
    @PostConstruct
    public void init(){
        try{
            InputStream serviceAccount = new ClassPathResource("static/serviceAccountKey.json").getInputStream();
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        } catch (Exception e){
            log.debug(e.getMessage());
            throw new ServerException(BaseException.FIREBASE_CONF_FAIL);
        }
    }

}
