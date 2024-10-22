package com.wasin.wasin.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "company_tb")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_id")
    private Long id;

    @Column(unique = true, name="fss_id")
    private String fssId;

    @Column
    private String name;

    @Column
    private String location;

    @Column(name = "is_auto")
    private Boolean isAuto;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @Builder
    public Company(Long id, String fssId, String name, String location, Boolean isAuto, Profile profile) {
        this.id = id;
        this.fssId = fssId;
        this.name = name;
        this.isAuto = isAuto;
        this.location = location;
        this.profile = profile;
    }

    public void addProfile(Profile profile) {
        this.profile = profile;
        this.lastUpdated = LocalDateTime.now();
    }

    public void changeModeAuto() {
        this.isAuto = true;
    }

    public void changeModeManual() {
        this.isAuto = false;
    }
}
