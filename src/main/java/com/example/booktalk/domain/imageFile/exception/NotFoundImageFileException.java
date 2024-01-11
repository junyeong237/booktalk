package com.example.booktalk.domain.imageFile.exception;

import com.example.booktalk.global.exception.ErrorCode;
import com.example.booktalk.global.exception.GlobalException;

public class NotFoundImageFileException extends GlobalException {

    public NotFoundImageFileException(ErrorCode errorCode) {
        super(errorCode);
    }
}

