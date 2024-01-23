package com.example.booktalk.domain.front;

import com.example.booktalk.domain.review.entity.Review;
import com.example.booktalk.domain.review.repository.ReviewRepository;
import com.example.booktalk.domain.review.service.ReviewService;
import com.example.booktalk.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class FrontReviewController {

    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;

    @GetMapping("/booktalk/reviews/list")
    public String reviewPage() {
        return "reviewlist";
    }

    @GetMapping("/booktalk/reviews/post/{productId}")
    public String postReviewPage(
        @PathVariable Long productId, Model model) {
        model.addAttribute("productId", productId);
        return "reviewForm";
    }

    @GetMapping("/booktalk/reviews/detail/{reviewId}")
    public String reviewDetailPage(@PathVariable Long reviewId, Model model) {
        model.addAttribute("reviewId", reviewId);
        return "reviewDetail";
    }

    @GetMapping("/booktalk/reviews/edit/{reviewId}")
    public String editReviewPage(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long reviewId, Model model) {

        Review review = reviewRepository.findReviewByIdWithThrow(reviewId);
        reviewService.validateReviewUser(userDetails.getUser(), review);
        model.addAttribute("productId", reviewId);
        return "reviewEdit";
    }

}
