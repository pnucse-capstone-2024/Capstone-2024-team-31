package com.wasin.wasin.domain.entity;

import com.wasin.wasin.domain.dto.RouterRequest;
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
@Table(name = "router_tb")
public class Router {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "router_id")
    private Long id;

    @Column(name = "mac_address", unique = true)
    private String macAddress;

    @Column(name = "router_name")
    private String name;

    @Column
    private String ssid;

    @Column
    private String instance;

    @Column
    private String job;

    @Column(name = "serial_number")
    private String serialNumber;

    @Column
    private String password;

    @Column
    private Integer port;

    @Column(name = "position_x")
    private Double positionX;

    @Column(name = "position_y")
    private Double positionY;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "company_id")
    private Company company;

    @Builder
    public Router(Long id, String macAddress, String name, String ssid, String instance, String job, String serialNumber,
                  String password, Integer port, Double positionX, Double positionY, Company company) {
        this.id = id;
        this.macAddress = macAddress;
        this.name = name;
        this.ssid = ssid;
        this.instance = instance;
        this.job = job;
        this.serialNumber = serialNumber;
        this.password = password;
        this.port = port;
        this.positionX = positionX;
        this.positionY = positionY;
        this.company = company;
    }

    public void updateColumns(RouterRequest.UpdateDTO requestDTO) {
        this.name = requestDTO.name();
        this.positionX = requestDTO.positionX();
        this.positionY = requestDTO.positionY();
        this.password = requestDTO.password();
    }
}
