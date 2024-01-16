package com.example.booktalk.domain.user.dto.request;

import jakarta.validation.constraints.Pattern;

public record UserProfileReq(
    String password,
    String nickname,
    String location,
    String description,
    @Pattern(regexp = "^[0-9]-*$",message = "번호를 다시 확인해주세요.")
    String phone
) {

}
