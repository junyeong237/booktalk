package com.example.booktalk.domain.chatRoom.exception;

import com.example.booktalk.global.exception.ErrorCode;
import com.example.booktalk.global.exception.GlobalException;

public class NotChatRoomUserException extends GlobalException {

    public NotChatRoomUserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
