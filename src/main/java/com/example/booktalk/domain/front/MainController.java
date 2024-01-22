package com.example.booktalk.domain.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/api/v2")
    public String mainPage() {
        return "index";
    }

}
