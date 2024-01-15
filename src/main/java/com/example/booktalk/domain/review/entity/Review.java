package com.example.booktalk.domain.review.entity;


import com.example.booktalk.domain.comment.entity.Comment;
import com.example.booktalk.domain.common.BaseEntity;
import com.example.booktalk.domain.review.dto.request.ReviewUpdateReq;


import com.example.booktalk.domain.user.entity.User;

import com.example.booktalk.domain.reviewlike.entity.ReviewLike;

import com.example.booktalk.domain.reviewlike.entity.ReviewLike;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(ReviewListener.class)
@Table(name = "TB_REVIEW")
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;



    @Column
    private Integer reviewLikeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private User user;


    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE)
    private List<Comment> commentList = new ArrayList<>();



    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE)
    private List<ReviewLike> reviewLikeList = new ArrayList<>();



    @Builder
    private Review(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    public void update(ReviewUpdateReq req) {
        this.title = req.title();
        this.content = req.content();
    }

    public void setReviewLikeCount(Integer reviewLikeCount) {
        this.reviewLikeCount = reviewLikeCount;
    }

    public void increaseReviewLike() {
        this.reviewLikeCount++;
    }

    public void decreaseReviewLike() {
        if(this.reviewLikeCount > 0) {
            this.reviewLikeCount--;
        }
    }
}
