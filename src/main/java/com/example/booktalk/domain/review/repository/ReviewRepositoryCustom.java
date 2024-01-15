package com.example.booktalk.domain.review.repository;

import com.example.booktalk.domain.review.entity.Review;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface ReviewRepositoryCustom {

    List<Review> getReviewListByTitleOrContent(Sort sort, String search);
}
