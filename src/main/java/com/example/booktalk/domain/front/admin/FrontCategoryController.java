package com.example.booktalk.domain.front.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontCategoryController {

    @GetMapping("/booktalk/admin/categories")
    public String categoryPage() {
        return "category"; // category.html이 있는 경로를 포함한 템플릿 이름을 반환
    }
}

