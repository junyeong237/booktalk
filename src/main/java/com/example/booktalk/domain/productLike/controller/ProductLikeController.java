package com.example.booktalk.domain.productLike.controller;

import com.example.booktalk.domain.product.dto.response.ProductListRes;
import com.example.booktalk.domain.productLike.dto.request.ProductLikeSwitchReq;
import com.example.booktalk.domain.productLike.dto.response.ProductLikeSwitchRes;
import com.example.booktalk.domain.productLike.service.ProductLikeService;
import com.example.booktalk.global.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/productLikes")
public class ProductLikeController {

    private final ProductLikeService productLikeService;

    @PutMapping
    public ResponseEntity<ProductLikeSwitchRes> switchProductLike(
        @RequestBody ProductLikeSwitchReq req,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ProductLikeSwitchRes productLikeRes = productLikeService.switchProductLike(req.productId(),
            userDetails.getUser().getId());

        return ResponseEntity.ok(productLikeRes);
    }

    @GetMapping
    public List<ProductListRes> getMyLikedProducts(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return productLikeService.getMyLikedProducts(userDetails.getUser().getId());
    }
}