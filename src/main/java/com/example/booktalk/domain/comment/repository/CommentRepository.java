package com.example.booktalk.domain.comment.repository;

import com.example.booktalk.domain.comment.entity.Comment;
import com.example.booktalk.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByReview(Review review);
}
