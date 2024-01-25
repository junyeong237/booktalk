package com.example.booktalk.domain.review.repository;

import com.example.booktalk.domain.review.entity.Review;
import com.example.booktalk.domain.review.exception.NotFoundReviewException;
import com.example.booktalk.domain.review.exception.ReviewErrorCode;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    List<Review> findAll(Sort sort);
    
    default Review findReviewByIdWithThrow(Long reviewId) {
        return findById(reviewId).orElseThrow(() ->
            new NotFoundReviewException(ReviewErrorCode.NOT_FOUND_REVIEW));
    }


}
