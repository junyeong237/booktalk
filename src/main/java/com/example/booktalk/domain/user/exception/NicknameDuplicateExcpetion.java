package com.example.booktalk.domain.user.exception;

import com.example.booktalk.global.exception.ErrorCode;
import com.example.booktalk.global.exception.GlobalException;

public class NicknameDuplicateExcpetion extends GlobalException {

    public NicknameDuplicateExcpetion(ErrorCode errorCode) {
        super(errorCode);
    }
}
