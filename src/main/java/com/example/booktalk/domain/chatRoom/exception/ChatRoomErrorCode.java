package com.example.booktalk.domain.chatRoom.exception;

import com.example.booktalk.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ChatRoomErrorCode implements ErrorCode {

    NOT_FOUND_CHAT_ROOM(HttpStatus.NOT_FOUND, "해당하는 채팅방이 없습니다");

    private final HttpStatus httpStatus;
    private final String message;
}
