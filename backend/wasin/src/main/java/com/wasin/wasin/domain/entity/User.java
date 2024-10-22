package com.wasin.wasin.domain.entity;


import com.wasin.wasin.domain.entity.enums.Role;
import com.wasin.wasin.domain.entity.enums.Status;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user_tb")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column
    private String username;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @Setter
    @Column(name = "lock_password")
    private String lockPassword;

    @Setter
    @Column(name = "fcm_token")
    private String fcmToken;

    @Column
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "is_mode_auto")
    private Boolean isModeAuto;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "company_id")
    private Company company;

    @Builder
    public User(Long id, String username, String email, String password, Role role, Status status, Boolean isModeAuto) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
        this.isModeAuto = isModeAuto;
    }

    public void makeActive() {
        this.status = Status.ACTIVE;
    }
    public void joinCompany(Company company) {
        this.company = company;
    }

    public void changeModeAuto() {
        this.isModeAuto = true;
    }

    public void changeModeManual() {
        this.isModeAuto = false;
    }

}
