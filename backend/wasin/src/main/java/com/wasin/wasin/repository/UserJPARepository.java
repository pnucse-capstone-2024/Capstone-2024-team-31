package com.wasin.wasin.repository;

import com.wasin.wasin.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserJPARepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query("select u from User u join fetch u.company c " +
            "where c.id = :companyId and u.status = 'STAND_BY' and u.role = 'ADMIN'")
    List<User> findAllStandbyAdminByCompanyId(@Param("companyId") Long companyId);

    @Query("select u from User u join fetch u.company c where c.id = :companyId ")
    List<User> findAllAdminByCompanyId(@Param("companyId") Long companyId);

    @Query("select u from User u join fetch u.company c where u.id = :userId")
    Optional<User> findUserWithCompanyById(@Param("userId") Long userId);

    @Query("select u from User u join fetch u.company c where u.id = :userId and u.status = 'ACTIVE'")
    Optional<User> findActiveUserWithCompanyById(@Param("userId") Long userId);
}
