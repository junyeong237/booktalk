package com.example.booktalk.domain.category.exception;

import com.example.booktalk.global.exception.ErrorCode;
import com.example.booktalk.global.exception.GlobalException;

public class ExistCategoryException extends GlobalException {

    public ExistCategoryException(ErrorCode errorCode) {
        super(errorCode);
    }

}
