package com.example.booktalk.domain.productLike.controller;

import com.example.booktalk.domain.productLike.dto.response.ProductLikeRes;
import com.example.booktalk.domain.productLike.service.ProductLikeService;
import com.example.booktalk.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products/{productId}/productLikes")
public class ProductLikeController {

    private final ProductLikeService productLikeService;

    @PatchMapping
    public ResponseEntity<ProductLikeRes> switchLikeProduct(@PathVariable Long productId,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ProductLikeRes productLikeRes = productLikeService.switchLikeProduct(productId, userDetails.getUser());

        return ResponseEntity.ok(productLikeRes);
    }
}