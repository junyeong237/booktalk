package com.example.booktalk.domain.user.dto.response;

public record UserProfileUpdateRes(
    Long id, String nickname, String email,
    String description,
    String location,
    String phone,
    String profileImagePathUrl
) {

}
