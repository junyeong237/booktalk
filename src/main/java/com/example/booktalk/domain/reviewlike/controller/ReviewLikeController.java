package com.example.booktalk.domain.reviewlike.controller;

import com.example.booktalk.domain.reviewlike.dto.request.ReviewLikeToggleReq;
import com.example.booktalk.domain.reviewlike.dto.response.ReviewLikeToggleRes;
import com.example.booktalk.domain.reviewlike.service.ReviewLikeService;
import com.example.booktalk.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/reviewLikes")
public class ReviewLikeController {

    private final ReviewLikeService reviewLikeService;

    @PostMapping
    public ResponseEntity<ReviewLikeToggleRes> toggleReviewLike(@RequestBody ReviewLikeToggleReq req,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(reviewLikeService.toggleReviewLike(
            req.reviewId(), userDetails.getUser().getId()));
    }

}
