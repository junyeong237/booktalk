package com.example.booktalk.domain.product.dto.request;

import com.example.booktalk.domain.product.entity.Region;
import java.util.List;

public record ProductUpdateReq(String name, Long price, Long quantity, Region region,
                               String content,
                               Boolean finished, List<String> categoryList) {

}
