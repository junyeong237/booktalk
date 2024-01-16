package com.example.booktalk.domain.user.exception;

import com.example.booktalk.global.exception.ErrorCode;
import com.example.booktalk.global.exception.GlobalException;

public class BlockedUserException extends GlobalException {

    public BlockedUserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
