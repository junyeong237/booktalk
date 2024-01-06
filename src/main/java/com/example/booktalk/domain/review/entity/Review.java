package com.example.booktalk.domain.review.entity;


import com.example.booktalk.domain.comment.entity.Comment;
import com.example.booktalk.domain.common.BaseEntity;
import com.example.booktalk.domain.review.dto.request.ReviewUpdateReq;
import com.example.booktalk.domain.reviewlike.entity.ReviewLike;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

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

    @ColumnDefault("0")
    @Column
    private Integer reviewLikeCount;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;

    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE)
    private List<Comment> commentList = new ArrayList<>();

    @Builder
    private Review(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void update(ReviewUpdateReq req) {
        this.title = req.title();
        this.content = req.content();
    }

    public void increaseReviewLike() {
        reviewLikeCount++;
    }

    public void decreaseReviewLike() {
        if(reviewLikeCount > 0) {
            reviewLikeCount--;
        }
    }
}
