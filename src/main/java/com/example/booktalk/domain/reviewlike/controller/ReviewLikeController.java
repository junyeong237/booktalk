package com.example.booktalk.domain.reviewlike.controller;

import com.example.booktalk.domain.reviewlike.dto.response.ReviewLiketoggleRes;
import com.example.booktalk.domain.reviewlike.service.ReviewLikeService;
import com.example.booktalk.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/reviews")
public class ReviewLikeController {

    private final ReviewLikeService reviewLikeService;

    @PostMapping("/{reviewId}/likes")
    public ResponseEntity<ReviewLiketoggleRes> toggleReviewLike(@PathVariable Long reviewId,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(reviewLikeService.toggleReviewLike(reviewId, userDetails.getUser().getId()));
    }

}
