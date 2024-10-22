package com.wasin.wasin._core.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.wasin.wasin._core.exception.BaseException;
import com.wasin.wasin._core.exception.error.ServerException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional
public class AwsFileUtil {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;

    public String upload(MultipartFile file) {
        String fileName =  UUID.randomUUID() + file.getName();
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        try {
            amazonS3Client.putObject(
                    new PutObjectRequest(bucket, fileName, file.getInputStream(), objectMetadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead));

            return amazonS3Client.getUrl(bucket, fileName).toString();
        } catch(IOException e) {
            throw new ServerException(BaseException.AWS_IMAGE_FAIL);
        }
    }

}
