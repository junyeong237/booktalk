package com.example.booktalk.domain.user.dto.request;

import jakarta.validation.constraints.Pattern;

public record UserProfileReq(
    String nickname,
    String location,
    String description,
    @Pattern(regexp = "^$|^[0-9]+(-[0-9]+)*$", message = "전화번호 양식을 맞춰주세요.")
    String phone
) {

}
