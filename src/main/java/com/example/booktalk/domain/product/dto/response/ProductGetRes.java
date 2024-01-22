package com.example.booktalk.domain.product.dto.response;

import com.example.booktalk.domain.imageFile.dto.response.ImageListRes;
import com.example.booktalk.domain.product.entity.Region;
import com.example.booktalk.domain.user.dto.response.UserRes;
import java.util.List;

public record ProductGetRes(
    Long id, String name, Long price, Long quantity, UserRes user,
    Region region, List<String> categories, Long productLikes,
    String content,
    Boolean finished,
    List<ImageListRes> imageListRes
) {

}
