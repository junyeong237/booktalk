package com.example.booktalk.domain.user.dto.response;

import com.example.booktalk.domain.imageFile.dto.response.ImageGetRes;

public record UserOwnProfileGetRes(Long id, String nickname, String email,
                                   String description,
                                   String location, String phone,
                                   ImageGetRes imageGetRes) {

}
