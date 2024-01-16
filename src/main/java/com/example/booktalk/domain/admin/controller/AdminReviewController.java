package com.example.booktalk.domain.admin.controller;

import com.example.booktalk.domain.admin.service.AdminReviewService;
import com.example.booktalk.domain.review.dto.response.ReviewDeleteRes;
import com.example.booktalk.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminReviewController {
    private final AdminReviewService adminReviewService;
    @DeleteMapping("/review/{reviewId}")
    public ResponseEntity<ReviewDeleteRes> deleteReview(
            @PathVariable(name = "reviewId") Long reviewId
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(adminReviewService.adminDeleteReview(reviewId));
    }
}
