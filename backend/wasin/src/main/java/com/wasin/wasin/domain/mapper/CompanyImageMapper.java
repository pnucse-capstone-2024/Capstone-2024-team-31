package com.wasin.wasin.domain.mapper;

import com.wasin.wasin._core.exception.BaseException;
import com.wasin.wasin._core.exception.error.ServerException;
import com.wasin.wasin.domain.entity.Company;
import com.wasin.wasin.domain.entity.CompanyImage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CompanyImageMapper {
    public CompanyImage urlToCompanyImage(String url, MultipartFile file, Company company) {
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            return CompanyImage.builder()
                    .url(url)
                    .company(company)
                    .width(image.getWidth())
                    .height(image.getHeight())
                    .build();
        } catch(IOException e) {
            throw new ServerException(BaseException.FILE_READ_FAIL);
        }
    }
}
