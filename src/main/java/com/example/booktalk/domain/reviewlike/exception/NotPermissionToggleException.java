package com.example.booktalk.domain.reviewlike.exception;

import com.example.booktalk.global.exception.ErrorCode;
import com.example.booktalk.global.exception.GlobalException;
import lombok.Getter;

@Getter
public class NotPermissionToggleException extends GlobalException {
    public NotPermissionToggleException(ErrorCode errorCode) {
        super(errorCode);
    }
}
