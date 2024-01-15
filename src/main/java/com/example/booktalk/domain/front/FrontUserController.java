package com.example.booktalk.domain.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontUserController {

    @GetMapping("/api/v1/users/profile")
    public String loginPage() {
        return "profile";
    }

}
