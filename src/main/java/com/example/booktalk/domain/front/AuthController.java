package com.example.booktalk.domain.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/api/v2/users/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/api/v2/users/signup")
    public String signupPage() {
        return "signup";
    }

}
