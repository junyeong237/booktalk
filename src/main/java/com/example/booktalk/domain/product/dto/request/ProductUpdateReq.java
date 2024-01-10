package com.example.booktalk.domain.product.dto.request;

import com.example.booktalk.domain.product.entity.Region;
import java.util.List;
import lombok.Builder;

@Builder
public record ProductUpdateReq(String name, Long price, Long quantity, Region region,
                               Boolean finished, List<String> categoryList) {

}
