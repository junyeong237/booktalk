package com.example.booktalk.domain.front;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class FrontProductController {

    @GetMapping("/booktalk/products/list")
    public String productsPage() {
        return "products";
    }

    @GetMapping("/booktalk/products/detail/{productId}")
    public String productPage(@PathVariable Long productId, Model model) {

        model.addAttribute("productId", productId);
        return "Detail";
    }

    @GetMapping("/booktalk/products/register")
    public String productUpdatePage() {

        return "productRegistration";
    }

    @GetMapping("/booktalk/products/{userId}/user")
    public String userProductsPage(@PathVariable Long userId, Model model) {

        model.addAttribute("userId", userId);
        return "otherProducts";
    }

}
