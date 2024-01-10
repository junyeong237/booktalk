package com.example.booktalk.domain.trade.dto.request;

public record TradeCreateReq(Long sellerId, Long productId, Long score) {
    //sellerId는 무조건 productId에 getUSer().getId()와 무조건 같은값이라는 전제가 꼭 필요
}
