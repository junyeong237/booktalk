package com.example.booktalk.domain.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontLikedProductsController {
    @GetMapping("/api/v2/LikedProducts")
    public String LikedProductsPage() {
        return "LikedProducts"; // category.html이 있는 경로를 포함한 템플릿 이름을 반환
    }
}

