package com.example.booktalk.domain.trade.exception;

import com.example.booktalk.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TradeErrorCode implements ErrorCode {

    NOT_FOUND_TRADE(HttpStatus.NOT_FOUND, "해당하는 거래가 존재하지 않습니다!");
    private final HttpStatus httpStatus;
    private final String message;
}

