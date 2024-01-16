package com.example.booktalk.domain.product.dto.response;

import com.example.booktalk.domain.imageFile.dto.response.ImageListRes;
import com.example.booktalk.domain.imageFile.entity.ImageFile;
import com.example.booktalk.domain.product.entity.Region;
import java.util.List;

public record ProductListRes(Long id, String name, Long price, Long quantity,
                             Long productLikes, List<String> categories,
                             Region region,
                             ImageListRes imageListRes) {


}
