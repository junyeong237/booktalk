package com.example.booktalk.domain.user.exception;

import com.example.booktalk.global.exception.ErrorCode;
import com.example.booktalk.global.exception.GlobalException;

public class InvalidPasswordCheckException extends GlobalException {

    public InvalidPasswordCheckException(ErrorCode errorCode) {
        super(errorCode);
    }
}
