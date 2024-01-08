package com.example.booktalk.domain.comment.exception;

import com.example.booktalk.global.exception.ErrorCode;
import com.example.booktalk.global.exception.GlobalException;
import lombok.Getter;

@Getter
public class NotPermissionCommentAuthorityException extends GlobalException {

    public NotPermissionCommentAuthorityException(ErrorCode errorCode) {
        super(errorCode);
    }
}
