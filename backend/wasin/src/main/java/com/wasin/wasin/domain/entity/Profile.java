package com.wasin.wasin.domain.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "profile_tb")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;

    @Column(unique = true, name = "profile_index")
    private Long index;

    @Column(length = 1000)
    private String title;

    @Column(length = 3000)
    private String description;

    @Column(length = 3000)
    private String tip;

    @Column(length = 3000)
    private String ssh;

    @Builder
    public Profile(Long id, Long index, String title, String description, String tip, String ssh) {
        this.id = id;
        this.index = index;
        this.title = title;
        this.description = description;
        this.tip = tip;
        this.ssh = ssh;
    }

}
