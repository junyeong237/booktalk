package com.example.booktalk.domain.comment.repository;

import com.example.booktalk.domain.comment.entity.Comment;
import com.example.booktalk.domain.comment.exception.CommentErrorCode;
import com.example.booktalk.domain.comment.exception.NotFoundCommentException;
import com.example.booktalk.domain.review.entity.Review;
import com.example.booktalk.domain.review.exception.NotFoundReviewException;
import com.example.booktalk.domain.review.exception.ReviewErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findAllByReviewOrderByCreatedAtDesc(Review review);

    default Comment findCommentByIdWithThrow(Long id) {
        return findById(id).orElseThrow(() ->
                new NotFoundCommentException(CommentErrorCode.NOT_FOUND_COMMENT));
    }
}
