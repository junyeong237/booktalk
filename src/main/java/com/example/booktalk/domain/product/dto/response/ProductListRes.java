package com.example.booktalk.domain.product.dto.response;

import com.example.booktalk.domain.product.entity.Region;

public record ProductListRes(Long id, String name, Long price, Long quantity,
                             Region region) {

}
