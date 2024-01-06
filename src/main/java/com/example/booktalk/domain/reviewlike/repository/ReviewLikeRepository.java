package com.example.booktalk.domain.reviewlike.repository;

import com.example.booktalk.domain.reviewlike.entity.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
}
