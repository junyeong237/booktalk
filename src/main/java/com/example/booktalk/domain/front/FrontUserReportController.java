package com.example.booktalk.domain.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontUserReportController {


    @GetMapping("/api/v1/report")
    public String reportPage() {
        return "userReport";
    }

}
