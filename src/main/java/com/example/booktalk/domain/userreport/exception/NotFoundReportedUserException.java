package com.example.booktalk.domain.userreport.exception;

import com.example.booktalk.domain.user.exception.NotFoundUserException;
import com.example.booktalk.global.exception.ErrorCode;
import com.example.booktalk.global.exception.GlobalException;
import org.aspectj.weaver.ast.Not;

public class NotFoundReportedUserException extends GlobalException {
    public NotFoundReportedUserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
