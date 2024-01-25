package com.example.booktalk.domain.review.repository;

import com.example.booktalk.domain.review.entity.Review;
import com.example.booktalk.domain.review.exception.NotFoundReviewException;
import com.example.booktalk.domain.review.exception.ReviewErrorCode;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    List<Review> findAll(Sort sort);

    List<Review> findByProductId(Long id, Sort sort);

    default Review findReviewByIdWithThrow(Long id) {
        return findById(id).orElseThrow(() ->
            new NotFoundReviewException(ReviewErrorCode.NOT_FOUND_REVIEW));
    }


}
