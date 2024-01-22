package com.example.booktalk.domain.admin.controller;

import com.example.booktalk.domain.admin.service.AdminProductService;
import com.example.booktalk.domain.product.dto.response.ProductDeleteRes;
import com.example.booktalk.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/admin/products")
@RequiredArgsConstructor
public class AdminProductController {
    private final AdminProductService adminProductService;
    @DeleteMapping("/{productId}")
    public ProductDeleteRes deleteProduct(
            @PathVariable Long productId
    ) {
        return adminProductService.adminDeleteProduct(productId);
    }
}
