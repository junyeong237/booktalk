package com.example.booktalk.domain.imageFile.exception;

import com.example.booktalk.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ImageFileErrorCode implements ErrorCode {

    NOT_FOUND_IMAGE(HttpStatus.NOT_FOUND, "해당하는 이미지가 존재하지 않습니다!");

    private final HttpStatus httpStatus;
    private final String message;
}


