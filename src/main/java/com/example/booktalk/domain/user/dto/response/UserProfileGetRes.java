package com.example.booktalk.domain.user.dto.response;

import com.example.booktalk.domain.imageFile.dto.response.ImageGetRes;

public record UserProfileGetRes(
    String nickname,
    String description,
    String location,
    ImageGetRes imageGetRes
) {

}
