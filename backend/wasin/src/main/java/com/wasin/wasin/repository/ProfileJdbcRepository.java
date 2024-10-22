package com.wasin.wasin.repository;

import com.wasin.wasin.domain.entity.Profile;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProfileJdbcRepository {

    private final JdbcTemplate jdbcTemplate;

    public void saveAll(List<Profile> profileList) {
        String sql = "INSERT INTO profile_tb (profile_index, title, description, tip, ssh) " +
                "VALUES (?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(
                sql,
                profileList,
                profileList.size(),
                (PreparedStatement ps, Profile profile) -> {
                    ps.setLong(1, profile.getIndex());
                    ps.setString(2, profile.getTitle());
                    ps.setString(3, profile.getDescription());
                    ps.setString(4, profile.getTip());
                    ps.setString(5, profile.getSsh());
                }
        );
    }
}
