package com.example.booktalk.domain.review.entity;


import com.example.booktalk.domain.common.BaseEntity;
import com.example.booktalk.domain.review.dto.request.ReviewUpdateReq;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_review")
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String content;

    @Builder
    private Review(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void update(ReviewUpdateReq req) {
        this.title = req.title();
        this.content = req.content();
    }
}
