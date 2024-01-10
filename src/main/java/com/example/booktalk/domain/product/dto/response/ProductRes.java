package com.example.booktalk.domain.product.dto.response;

import com.example.booktalk.domain.product.entity.Product;
import com.example.booktalk.domain.user.dto.response.UserRes;
import com.example.booktalk.domain.user.entity.User;

public record ProductRes(Long id, String name, Long price, UserRes user) {

    public ProductRes(User seller, Product product) {
        this(product.getId(), product.getName(),
            product.getPrice(),
            new UserRes(seller.getId(), seller.getNickname()));
    }

}
