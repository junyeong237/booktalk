package com.example.booktalk.domain.admin.controller;

import com.example.booktalk.domain.review.dto.response.ReviewDeleteRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/reviews")
@RequiredArgsConstructor
public class AdminReviewController {
    private final AdminReviewService adminReviewService;
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ReviewDeleteRes> deleteReview(
            @PathVariable Long reviewId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(adminReviewService.adminDeleteReview(reviewId));
    }
}
