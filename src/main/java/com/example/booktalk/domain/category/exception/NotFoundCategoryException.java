package com.example.booktalk.domain.category.exception;

import com.example.booktalk.global.exception.ErrorCode;
import com.example.booktalk.global.exception.GlobalException;

public class NotFoundCategoryException extends GlobalException {

    public NotFoundCategoryException(ErrorCode errorCode) {
        super(errorCode);
    }

}
