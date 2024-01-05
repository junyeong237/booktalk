package com.example.booktalk.domain.review.repository;

import com.example.booktalk.domain.review.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
