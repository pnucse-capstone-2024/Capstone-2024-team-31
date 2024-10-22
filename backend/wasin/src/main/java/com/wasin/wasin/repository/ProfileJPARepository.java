package com.wasin.wasin.repository;

import com.wasin.wasin.domain.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileJPARepository extends JpaRepository<Profile, Long> {
    Optional<Profile> findByIndex(Long profileIndex);

    Optional<Profile> findBySsh(String ssh);
}
