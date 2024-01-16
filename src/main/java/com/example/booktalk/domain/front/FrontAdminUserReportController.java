package com.example.booktalk.domain.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontAdminUserReportController {

    @GetMapping("/api/v1/admin/report")
    public String reportListPage() {
        return "userReportList";
    }
}
