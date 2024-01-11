package com.example.booktalk.domain.chatRoom.exception;

import com.example.booktalk.global.exception.ErrorCode;
import com.example.booktalk.global.exception.GlobalException;

public class NotFoundChatRoomException extends GlobalException {

    public NotFoundChatRoomException(ErrorCode errorCode) {
        super(errorCode);
    }
}

