package com.example.booktalk.domain.front;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontProductController {

    @GetMapping("/api/v1/products/list")
    public String productsPage() {
        return "products";
    }

    @GetMapping("/api/v1/products/detail/{productId}")
    public String productPage() {
        return "productDetail";
    }
}
