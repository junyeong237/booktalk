package com.example.booktalk.domain.front;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FrontReviewController {

    @GetMapping("/api/v1/reviews/list")
    public String reviewPage() {
        return "reviewlist";
    }

    @GetMapping("/api/v1/reviews/post/{productId}")
    public String postReviewPage(
        @PathVariable Long productId, Model model) {
        model.addAttribute("productId", productId);
        return "reviewForm";
    }

    @GetMapping("api/v1/reviews/detail/{reviewId}")
    public String reviewDetailPage(@PathVariable Long reviewId, Model model) {
        model.addAttribute("reviewId", reviewId);
        return "reviewDetail";
    }

    @GetMapping("/api/v1/reviews/edit/{reviewId}")
    public String editReviewPage(
        @PathVariable Long reviewId, Model model) {
        model.addAttribute("productId", reviewId);
        return "reviewEdit";
    }

}
