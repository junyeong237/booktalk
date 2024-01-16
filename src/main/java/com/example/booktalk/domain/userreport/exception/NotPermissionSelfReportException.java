package com.example.booktalk.domain.userreport.exception;

import com.example.booktalk.global.exception.ErrorCode;
import com.example.booktalk.global.exception.GlobalException;

public class NotPermissionSelfReportException extends GlobalException {
    public NotPermissionSelfReportException(ErrorCode errorCode) {
        super(errorCode);
    }
}
