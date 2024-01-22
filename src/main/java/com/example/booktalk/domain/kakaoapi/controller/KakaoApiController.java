package com.example.booktalk.domain.kakaoapi;

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
@RequestMapping("/api/v2/products")
public class KakaoApiController {

    private final KakaoApiService kakaoApiService;

    @GetMapping("/kakao")
    public List<ProductApiListRes> getProductByKakao(@RequestParam(value = "query") String query) {
        return kakaoApiService.getProductWithKakaoApi(query);

    }


}
