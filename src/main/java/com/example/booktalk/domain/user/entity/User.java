package com.example.booktalk.domain.user.entity;

import com.example.booktalk.domain.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "TB_USER")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String nickname;

    @Column
    private String location;

    @Column
    private String phone;

    @Column
    private String description;

    @Column
    private boolean deleted;

    @Column
    @Enumerated(value = EnumType.STRING)
    private UserRoleType role;

    @Builder
    public User(String email, String password, UserRoleType role, String randomNickname) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.nickname = randomNickname;
    }

    public void updateProfile(String newPassword, String description, String phone, String location,
        String nickname) {
        this.password = newPassword;
        this.description = description;
        this.phone = phone;
        this.location = location;
        this.nickname = nickname;
    }

    public void updateRole(UserRoleType role) {
        this.role=role;
    }
}
