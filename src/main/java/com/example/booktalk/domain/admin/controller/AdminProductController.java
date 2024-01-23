package com.example.booktalk.domain.admin.controller;

import com.example.booktalk.domain.product.dto.response.ProductDeleteRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ProductDeleteRes> deleteProduct(
        @PathVariable Long productId
    ) {
        ProductDeleteRes res = adminProductService.adminDeleteProduct(productId);
        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
