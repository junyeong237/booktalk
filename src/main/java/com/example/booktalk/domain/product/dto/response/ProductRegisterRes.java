package com.example.booktalk.domain.product.dto.response;

import com.example.booktalk.domain.product.entity.Region;
import java.util.List;

public record ProductRegisterRes(Long id, String name, Long quantity,
                                 List<Region> regions, Boolean finished, User user) {
//
//    public ProductRegisterRes(Product product) {
//        this.id = product.getId();
//        this.finished = product.getFinished();
//        this.name = product.getName();
//        this.quantity = product.getQuantity();
//        this.regions = product.getRegions();
//        this.user = product.getUser();
//    }

}
