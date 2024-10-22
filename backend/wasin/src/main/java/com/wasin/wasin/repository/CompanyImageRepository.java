package com.wasin.wasin.repository;

import com.wasin.wasin.domain.entity.CompanyImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyImageRepository extends JpaRepository<CompanyImage, Long> {

    Optional<CompanyImage> findByCompanyId(Long companyId);

    void deleteByCompanyId(Long companyId);
}
