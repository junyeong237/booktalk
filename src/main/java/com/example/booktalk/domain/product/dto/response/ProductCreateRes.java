package com.example.booktalk.domain.product.dto.response;

import com.example.booktalk.domain.imageFile.dto.response.ImageCreateRes;
import com.example.booktalk.domain.product.entity.Region;
import com.example.booktalk.domain.user.dto.response.UserRes;
import java.util.List;

public record ProductCreateRes(
    Long id, String name, Long quantity, Long price,
    Region region, Boolean finished, UserRes user, String content,
    List<String> categoryList,
    List<ImageCreateRes> imageCreateResList
) {

}
