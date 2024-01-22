package com.example.booktalk.domain.user.entity;

import com.example.booktalk.domain.common.BaseEntity;
import com.example.booktalk.domain.trade.entity.Trade;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
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
    private String profileImagePathUrl;

    @Column
    @Enumerated(value = EnumType.STRING)
    private UserRoleType role;

    @Column
    private Double score;

    @Column
    private Integer reportCount;

    @Column
    private Long kakaoId;

    @OneToMany(mappedBy = "seller")
    private List<Trade> tradeList = new ArrayList<>();
    ;

    @Builder
    public User(String email, String password, UserRoleType role, String randomNickname,
        String profileImagePathUrl) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.nickname = randomNickname;
        this.reportCount = 0;
        this.profileImagePathUrl = profileImagePathUrl;
        this.score = 7.0;
    }

    public User(String nickname, String encodedPassword, String email, UserRoleType userRoleType,
        Long kakaoId) {
        this.nickname = nickname;
        this.password = encodedPassword;
        this.email = email;
        this.role = userRoleType;
        this.kakaoId = kakaoId;
    }

    public void updateProfile(String description, String phone, String location,
        String nickname, String profileImagePathUrl) {
        this.description = description;
        this.phone = phone;
        this.location = location;
        this.nickname = nickname;
        this.profileImagePathUrl = profileImagePathUrl;
    }

    public void updateRole(UserRoleType role) {
        this.role = role;
    }

    public void averageScore() {
        if (!tradeList.isEmpty()) {
            Double sum = 7.0;
            for (Trade trade : tradeList) {
                sum += trade.getScore();
            }
            this.score = Math.round((sum / (tradeList.size() + 1)) * 10.0) / 10.0;
        } else {
            this.score = 7.0;
        }
    }


    public User kakaoIdUpdate(Long kakaoId) {
        this.kakaoId = kakaoId;
        return this;
    }

    public void withdraw() {
        this.deleted = true;
    }


    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void increaseReportCount() {
        this.reportCount++;
    }

}
