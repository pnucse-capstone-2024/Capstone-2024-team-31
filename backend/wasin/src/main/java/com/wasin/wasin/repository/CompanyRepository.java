package com.wasin.wasin.repository;

import com.wasin.wasin.domain.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByFssId(String fssId);

    @Query("SELECT c FROM Company c join fetch c.profile p " +
            "where c.isAuto = true and p.index = 3")
    List<Company> findAutoPowerSavingCompany();

}
