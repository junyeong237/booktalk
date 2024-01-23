package com.example.booktalk.domain.front.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontUserBlockController {

    @GetMapping("/booktalk/admin/userBlock")
    public String categoryPage() {
        return "userBlock";
    }
}

