package com.example.booktalk.domain.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontReviewController {
    @GetMapping("/api/v1/reviews/list")
    public String reviewPage() {
        return "reviewlist";
    }

    @GetMapping("/api/v1/reviews/post")
    public String reviewPost() {
        return "createReview";
    }
}
