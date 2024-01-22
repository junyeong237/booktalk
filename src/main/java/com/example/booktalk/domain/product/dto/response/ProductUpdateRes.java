package com.example.booktalk.domain.product.dto.response;

import com.example.booktalk.domain.imageFile.dto.response.ImageCreateRes;
import com.example.booktalk.domain.product.entity.Region;
import com.example.booktalk.domain.user.dto.response.UserRes;
import java.util.List;

public record ProductUpdateRes(
    Long id, String name, Long quantity, Long price,
    Region regions, Boolean finished, UserRes user, Long productLikes,
    String content,
    List<String> categoryList,
    List<ImageCreateRes> imageCreateResList
) {

}
