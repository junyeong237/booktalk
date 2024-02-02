package com.example.booktalk.domain.user.exception;

import com.example.booktalk.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    NOT_PERMISSION_TOKEN(HttpStatus.FORBIDDEN, "토큰문제가 발생하였습니다. 쿠키를 지우거나 시크릿탭으로 접속해주세요"),
    FORBIDDEN_ACCESS_PROFILE(HttpStatus.FORBIDDEN, "본인만 수정할 수 있습니다."),
    FORBIDDEN_BLOCKED_USER(HttpStatus.FORBIDDEN, "차단된 유저입니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "해당 회원을 찾을 수 없습니다."),
    NOT_FOUND_REFRESH_TOKEN(HttpStatus.NOT_FOUND, "해당 리프레시 토큰을 찾을 수 없습니다."),
    NICKNAME_DUPLICATE(HttpStatus.BAD_REQUEST, "이미 사용중인 닉네임입니다."),
    ALREADY_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "이미 가입된 이메일입니다."),
    INVALID_ADMIN_CODE(HttpStatus.BAD_REQUEST, "관리자 인증 번호가 틀렸습니다."),
    BAD_LOGIN(HttpStatus.BAD_REQUEST, "패스워드를 확인해주세요."),
    NOT_MATCH_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    INVALID_PASSWORD_CHECK(HttpStatus.BAD_REQUEST, "비밀번호와 비밀번호 확인이 일치하지 않습니다."),
    NOT_IMAGE_FILE(HttpStatus.NOT_FOUND, "프로필 이미지를 선택 하세요."),
    FORBIDDEN_NOT_BLOCK_SELF(HttpStatus.FORBIDDEN, "스스로를 차단할 수 없습니다."),
    FORBIDDEN_NOT_BLOCK_ADMIN(HttpStatus.FORBIDDEN, "관리자를 차단할 수 없습니다."),
    WITHDRAWN_USER(HttpStatus.NOT_FOUND, "탈퇴한 회원 입니다.");
    private final HttpStatus httpStatus;
    private final String message;
}
