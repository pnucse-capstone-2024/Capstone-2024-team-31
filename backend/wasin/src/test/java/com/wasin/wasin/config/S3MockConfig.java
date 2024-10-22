package com.wasin.wasin.config;

import com.amazonaws.services.s3.AmazonS3Client;
import com.wasin.wasin._core.config.AwsS3Config;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Profile("test")
@TestConfiguration
public class S3MockConfig extends AwsS3Config {

    @Override
    @Bean(name = "new-amazonS3")
    @Primary
    public AmazonS3Client amazonS3Client(){
        return Mockito.mock(AmazonS3Client.class);
    }
}
