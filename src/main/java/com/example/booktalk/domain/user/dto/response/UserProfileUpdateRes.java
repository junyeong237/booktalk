package com.example.booktalk.domain.user.dto.response;

import com.example.booktalk.domain.imageFile.dto.response.ImageCreateRes;

public record UserProfileUpdateRes(
    Long id, String nickname, String email,
    String description,
    String location,
    String phone,
    ImageCreateRes imageCreateRes
) {

}
