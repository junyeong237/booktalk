package com.example.booktalk.domain.product.dto.response;

import com.example.booktalk.domain.product.entity.Region;
import com.example.booktalk.domain.user.entity.User;
import java.util.List;

public record ProductRegisterRes(Long id, String name, Long quantity,
                                 List<Region> regions, Boolean finished, User user,
                                 List<String> categoryList) {


}
