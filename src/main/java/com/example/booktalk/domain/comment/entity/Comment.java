package com.example.booktalk.domain.comment.entity;

import com.example.booktalk.domain.common.BaseEntity;
import com.example.booktalk.domain.review.entity.Review;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tb_comment")
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Review review;

    @Builder
    private Comment(String content, Review review) {
        this.content = content;
        this.review = review;
    }
}
