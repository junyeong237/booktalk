package com.example.booktalk.domain.product.dto.request;

import com.example.booktalk.domain.product.entity.Region;
import java.util.List;
import lombok.Builder;

@Builder
public record ProductRegisterReq(String name, Long price, Long quantity, List<Region> regions,
                                 Boolean finished) {

}
