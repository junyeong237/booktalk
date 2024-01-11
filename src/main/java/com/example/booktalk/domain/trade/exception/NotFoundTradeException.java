package com.example.booktalk.domain.trade.exception;

import com.example.booktalk.global.exception.ErrorCode;
import com.example.booktalk.global.exception.GlobalException;

public class NotFoundTradeException extends GlobalException {

    public NotFoundTradeException(ErrorCode errorCode) {
        super(errorCode);
    }
}

