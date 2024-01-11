package com.example.booktalk.domain.product.dto.response;

import com.example.booktalk.domain.product.entity.Product;
import com.example.booktalk.domain.product.entity.Region;

public record ProductTagListRes(Long id, String name, Long price, Long quantity,
                                Long productLikes,
                                Region region) {

    public ProductTagListRes(Product product) {
        this(product.getId(), product.getName(), product.getPrice(), product.getQuantity(),
            product.getProductLikeCnt(), product.getRegion());
    }

}
