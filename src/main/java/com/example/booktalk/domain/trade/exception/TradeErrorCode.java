package com.example.booktalk.domain.trade.exception;

import com.example.booktalk.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TradeErrorCode implements ErrorCode {

    NOT_FOUND_TRADE(HttpStatus.NOT_FOUND, "해당하는 거래가 존재하지 않습니다!"),
    INVALIE_SCORE_INPUT(HttpStatus.BAD_REQUEST, "평점은 1이상 10이하의 정수로 입력해주세요"),
    NOT_PERMISSION_REGITSTER_TRADE(HttpStatus.FORBIDDEN, "판매자는 본인의 상품을 구매할 수 없습니다."),
    FORBIDDEN_READ_TRADE_LIST(HttpStatus.FORBIDDEN, "자신의 거래내역이 아니라 확인 할 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}

