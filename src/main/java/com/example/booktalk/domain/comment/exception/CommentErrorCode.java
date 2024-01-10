package com.example.booktalk.domain.comment.exception;

import com.example.booktalk.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommentErrorCode implements ErrorCode {

    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "해당하는 댓글이 존재하지 않습니다."),
    NOT_PERMISSION_COMMENT_AUTHORITY(HttpStatus.FORBIDDEN, "댓글 수정 및 삭제 권한이 없습니다."),
    EMPTY_CONTENT(HttpStatus.NO_CONTENT, "댓글을 입력해주세요.");

    private final HttpStatus httpStatus;
    private final String message;
}
