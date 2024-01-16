package com.example.booktalk.domain.product.dto.response;

import com.example.booktalk.domain.product.entity.Region;
import java.util.List;

public record ProductTopLikesListRes(Long id, String name, Long price, Long quantity,
                                     Long productLikes, List<String> categories,
                                     Region region) {

}