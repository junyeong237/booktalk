package com.example.booktalk.domain.product.exception;

import com.example.booktalk.global.exception.ErrorCode;
import com.example.booktalk.global.exception.GlobalException;

public class NotPermissionAuthority extends GlobalException {

    public NotPermissionAuthority(ErrorCode errorCode) {
        super(errorCode);
    }
}

