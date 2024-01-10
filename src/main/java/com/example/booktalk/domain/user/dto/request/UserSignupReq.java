package com.example.booktalk.domain.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserSignupReq(
    @Email(message = "이메일 형식을 지켜주세요")
    String email,
    @Size(min = 4, max = 15, message = "비밀번호는 4~15자만 가능합니다.")
    @Pattern(regexp = "^[a-zA-Z_0-9]*$", message = "알파벳과 숫자만 비밀번호 설정이 가능합니다.")
    String password,
    String passwordCheck,
    boolean admin,
    String adminToken
) {

}
