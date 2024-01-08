package com.example.booktalk.domain.user.exception;

import com.example.booktalk.global.exception.ErrorCode;
import com.example.booktalk.global.exception.GlobalException;

public class AlreadyExistEmailException extends GlobalException {

    public AlreadyExistEmailException(ErrorCode errorCode) {
        super(errorCode);
    }
}
