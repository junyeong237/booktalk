package com.example.booktalk.domain.trade.exception;

import com.example.booktalk.global.exception.ErrorCode;
import com.example.booktalk.global.exception.GlobalException;

public class NotPermissionRegisterTrade extends GlobalException {

    public NotPermissionRegisterTrade(ErrorCode errorCode) {
        super(errorCode);
    }
}
