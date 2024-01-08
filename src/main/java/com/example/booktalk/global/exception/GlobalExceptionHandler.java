package com.example.booktalk.global.exception;

import java.util.ArrayList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<Object> handleException(GlobalException exception) {
        ErrorCode errorCode = exception.getErrorCode();

        ErrorResponse response = ErrorResponse.builder()
            .status(errorCode.getHttpStatus().value())
            .build();
        response.addMessage(errorCode.getMessage());

        return ResponseEntity.status(errorCode.getHttpStatus())
            .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException e) {

        ArrayList<String> errors = new ArrayList<>();
        e.getAllErrors().forEach(error -> errors.add(error.getDefaultMessage()));

        ErrorResponse response = ErrorResponse.builder()
            .messages(errors)
            .status(400)
            .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(response);

    }

}
