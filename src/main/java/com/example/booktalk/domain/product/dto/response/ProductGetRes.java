package com.example.booktalk.domain.product.dto.response;

import com.example.booktalk.domain.product.entity.Region;
import com.example.booktalk.domain.user.dto.response.UserRes;

public record ProductGetRes(Long id, String name, Long price, Long quantity, UserRes user,
                            Region region, Boolean finished) {

}
