package com.example.booktalk.domain.kakaoapi.controller;

import com.example.booktalk.domain.kakaoapi.service.KakaoApiService;
import com.example.booktalk.domain.product.dto.response.ProductApiListRes;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KakaoApiController {

    private final KakaoApiService kakaoApiService;

    @GetMapping("/api/v2/products/kakao")
    public ResponseEntity<List<ProductApiListRes>> getProductByKakao(
        @RequestParam(value = "query") String query
    ) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(kakaoApiService.getProductWithKakaoApi(query));
    }


}
