package com.example.booktalk.domain.comment.exception;

import com.example.booktalk.global.exception.ErrorCode;
import com.example.booktalk.global.exception.GlobalException;

public class NotFoundCommentException extends GlobalException {

    public NotFoundCommentException(ErrorCode errorCode) {
        super(errorCode);
    }
}
