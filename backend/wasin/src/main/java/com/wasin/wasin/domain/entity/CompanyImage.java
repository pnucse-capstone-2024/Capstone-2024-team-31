package com.wasin.wasin.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "company_image_tb")
public class CompanyImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "company_image_id")
    private Long id;

    @Column(length = 1000)
    private String url;

    @Column
    private int width;

    @Column
    private int height;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "company_id")
    private Company company;

    @Builder
    public CompanyImage(Long id, String url, Company company, int width, int height) {
        this.id = id;
        this.url = url;
        this.company = company;
        this.width = width;
        this.height = height;
    }
}
