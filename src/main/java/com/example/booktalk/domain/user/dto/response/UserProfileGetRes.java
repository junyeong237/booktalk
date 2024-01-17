package com.example.booktalk.domain.user.dto.response;

public record UserProfileGetRes(
    Long id,
    String nickname,
    String description,
    String location,

    String email,
    Double score,

    String profileImagePathUrl
) {

}
