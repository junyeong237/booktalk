package com.example.booktalk.domain.user.exception;

import com.example.booktalk.global.exception.ErrorCode;
import com.example.booktalk.global.exception.GlobalException;

public class NotPermissionTokenException extends GlobalException {

    public NotPermissionTokenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
