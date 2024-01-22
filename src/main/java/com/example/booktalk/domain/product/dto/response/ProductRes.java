package com.example.booktalk.domain.product.dto.response;

import com.example.booktalk.domain.user.dto.response.UserRes;

public record ProductRes(
    Long id, String name, Long price, UserRes user
) {

}
