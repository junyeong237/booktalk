package com.example.booktalk.domain.front;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FrontProductController {

    @GetMapping("/api/v2/products/list")
    public String productsPage() {
        return "products";
    }

    @GetMapping("/api/v2/products/detail/{productId}")
    public String productPage(@PathVariable Long productId, Model model) {

        model.addAttribute("productId", productId);
        return "Detail";
    }

    @GetMapping("/api/v2/products/register")
    public String productUpdatePage() {

        return "productRegistration";
    }

}
