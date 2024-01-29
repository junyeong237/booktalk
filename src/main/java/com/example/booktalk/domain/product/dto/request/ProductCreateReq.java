package com.example.booktalk.domain.product.dto.request;

import com.example.booktalk.domain.product.entity.Region;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;


public record ProductCreateReq(@NotBlank(message = "상품이름을 입력해주세요") String name,
                               @NotNull(message = "상품가격을 입력해주세요") Long price,
                               @NotNull(message = "상품수량을 입력해주세요") Long quantity,
                               Region region,
                               @NotBlank(message = "상품내용을 입력해주세요") String content,
                               List<String> categoryList) {

}
