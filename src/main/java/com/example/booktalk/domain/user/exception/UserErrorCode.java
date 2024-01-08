package com.example.booktalk.domain.user.exception;

import com.example.booktalk.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    ALREADY_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "이미 가입된 이메일입니다."),
    INVALID_ADMIN_CODE(HttpStatus.BAD_REQUEST,"관리자 인증 번호가 틀렸습니다."),
    BAD_LOGIN(HttpStatus.BAD_REQUEST,"이메일 또는 패스워드를 확인해주세요."),
    INVALID_PASSWORD_CHECK(HttpStatus.BAD_REQUEST,"비밀번호와 비밀번호 확인이 일치하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
