package com.example.booktalk.domain.product.service;

import com.example.booktalk.domain.product.dto.request.ProductCreateReq;
import com.example.booktalk.domain.product.dto.response.ProductDeleteRes;


public class ProdcutService {


    ProductCreateReq req = ProductCreateReq.builder()
        .name("테스트")
        .price(10L)
        .description("테스트용")
        .build();

}
