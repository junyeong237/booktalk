package com.example.booktalk.domain.trade.exception;

import com.example.booktalk.global.exception.ErrorCode;
import com.example.booktalk.global.exception.GlobalException;

public class ForbiddenReadTradeListException extends GlobalException {

    public ForbiddenReadTradeListException(ErrorCode errorCode) {
        super(errorCode);
    }
}
