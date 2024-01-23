package com.example.booktalk.domain.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontLikedProductsController {

    @GetMapping("/booktalk/LikedProducts")
    public String LikedProductsPage() {
        return "likedProducts"; // category.html이 있는 경로를 포함한 템플릿 이름을 반환
    }
}

