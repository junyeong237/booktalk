package com.example.booktalk.domain.userreport.entity;

import com.example.booktalk.domain.common.BaseEntity;
import com.example.booktalk.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "TB_USER_REPORT")
public class UserReport extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String reason;

    @ManyToOne
    @JoinColumn(name = "reported_user_id")
    private User reportedUser;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User reportUser;

    @Builder
    private UserReport(String reason, User reportedUser, User reportUser) {
        this.reason = reason;
        this.reportedUser = reportedUser;
        this.reportUser = reportUser;
    }

}
