package com.example.booktalk.domain.productLike.controller;

import com.example.booktalk.domain.product.dto.response.ProductListRes;
import com.example.booktalk.domain.productLike.dto.request.SwitchProductLikeReq;
import com.example.booktalk.domain.productLike.dto.response.ProductLikeRes;
import com.example.booktalk.domain.productLike.service.ProductLikeService;
import com.example.booktalk.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/productLikes")
public class ProductLikeController {

    private final ProductLikeService productLikeService;

    @PutMapping
    public ResponseEntity<ProductLikeRes> switchProductLike(@RequestBody SwitchProductLikeReq req,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ProductLikeRes productLikeRes = productLikeService.switchProductLike(req.productId(), userDetails.getUser().getId());

        return ResponseEntity.ok(productLikeRes);
    }

    @GetMapping
    public List<ProductListRes> getMyLikedProducts(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return productLikeService.getMyLikedProducts(userDetails.getUser().getId());
    }
}