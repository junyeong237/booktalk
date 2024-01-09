package com.example.booktalk.domain.user.dto.response;

public record UserProfileGetRes(
    String nickname,
    String description,
    String location
) {

}
