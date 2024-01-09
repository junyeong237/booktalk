package com.example.booktalk.domain.productLike.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProductLikeRes {
    private Boolean isProductLiked;

    // 좋아요 응답 DTO 생성자

    @Builder
    public ProductLikeRes(Boolean isProductLiked) {
        this.isProductLiked = isProductLiked;
    }
}
