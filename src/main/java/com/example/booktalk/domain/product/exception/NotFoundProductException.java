package com.example.booktalk.domain.product.exception;

import com.example.booktalk.global.exception.ErrorCode;
import com.example.booktalk.global.exception.GlobalException;
import lombok.Getter;

@Getter
public class NotFoundProductException extends GlobalException {

    public NotFoundProductException(ErrorCode errorCode) {
        super(errorCode);
    }
}
