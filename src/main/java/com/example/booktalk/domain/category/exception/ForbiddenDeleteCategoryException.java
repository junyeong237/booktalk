package com.example.booktalk.domain.category.exception;

import com.example.booktalk.global.exception.ErrorCode;
import com.example.booktalk.global.exception.GlobalException;

public class ForbiddenDeleteCategoryException extends GlobalException {

    public ForbiddenDeleteCategoryException(ErrorCode errorCode) {
        super(errorCode);
    }

}
