package com.example.booktalk.domain.front.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class FrontUserBlockController {
    @GetMapping("/api/v2/admin/userBlock")
    public String categoryPage() {
        return "userBlock";
    }
}

