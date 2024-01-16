package com.example.booktalk.domain.productLike.controller;

import com.example.booktalk.domain.productLike.dto.request.SwitchProductLikeReq;
import com.example.booktalk.domain.productLike.dto.response.ProductLikeRes;
import com.example.booktalk.domain.productLike.service.ProductLikeService;
import com.example.booktalk.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ProductLikeController {

    private final ProductLikeService productLikeService;

    @PatchMapping("/productLikes")
    public ResponseEntity<ProductLikeRes> switchProductLike(@RequestBody SwitchProductLikeReq req,
                                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ProductLikeRes productLikeRes = productLikeService.switchProductLike(req.productId(), userDetails.getUser().getId());

        return ResponseEntity.ok(productLikeRes);
    }
}