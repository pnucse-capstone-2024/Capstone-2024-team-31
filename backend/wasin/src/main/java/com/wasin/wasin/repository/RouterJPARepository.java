package com.wasin.wasin.repository;

import com.wasin.wasin.domain.entity.Router;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RouterJPARepository extends JpaRepository<Router, Long> {

    Optional<Router> findByMacAddress(String macAddress);

    @Query("select r from Router r join fetch r.company c join fetch c.profile where r.instance = :instance")
    Optional<Router> findByInstance(@Param("instance") String instance);

    @Query("select r from Router r join fetch r.company c where c.id = :companyId")
    List<Router> findAllRouterByCompanyId(@Param("companyId") Long companyId);

}
