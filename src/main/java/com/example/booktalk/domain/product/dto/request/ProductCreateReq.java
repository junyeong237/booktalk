package com.example.booktalk.domain.product.dto.request;

import lombok.Builder;

@Builder
public record ProductCreateReq(String name, Long price, String description) {

}
