package com.example.booktalk.domain.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontTradeController {

    @GetMapping("api/v2/myTrade")
    public String tradesPage() {
        return "myTrade";
    }
}
