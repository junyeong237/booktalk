package com.example.booktalk.domain.productLike.exception;

import com.example.booktalk.global.exception.ErrorCode;
import com.example.booktalk.global.exception.GlobalException;

public class NotPermissionMineException extends GlobalException {

    public NotPermissionMineException(ErrorCode errorCode) {
        super(errorCode);
    }
}
