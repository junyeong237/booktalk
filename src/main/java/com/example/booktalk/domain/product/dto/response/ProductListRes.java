package com.example.booktalk.domain.product.dto.response;

import com.example.booktalk.domain.product.entity.Region;
import java.util.List;

public record ProductListRes(Long id, String name, Long price, Long quantity,
                             List<Region> regions) {

}