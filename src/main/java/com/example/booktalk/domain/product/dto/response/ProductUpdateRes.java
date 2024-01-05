package com.example.booktalk.domain.product.dto.response;

import com.example.booktalk.domain.product.entity.Region;
import java.util.List;
import lombok.Getter;

@Getter
public record ProductUpdateRes(Long id, String name, Long quantity,
                               List<Region> regions, Boolean finished) {
//
//    public ProductUpdateRes(Product product) {
//        this.id = product.getId();
//        this.finished = product.getFinished();
//        this.name = product.getName();
//        this.quantity = product.getQuantity();
//        this.regions = product.getRegions();
//    }
}
