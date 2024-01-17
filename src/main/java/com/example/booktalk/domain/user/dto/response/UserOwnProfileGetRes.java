package com.example.booktalk.domain.user.dto.response;

public record UserOwnProfileGetRes(Long id, String nickname, String email,
                                   String description,
                                   String location, String phone, Double score,

                                   String profileImagePathUrl) {

}
