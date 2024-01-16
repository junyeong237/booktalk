package com.example.booktalk.domain.comment.exception;

import com.example.booktalk.global.exception.ErrorCode;
import com.example.booktalk.global.exception.GlobalException;

public class EmptyContentException extends GlobalException {

    public EmptyContentException(ErrorCode errorCode) {
        super(errorCode);
    }
}
