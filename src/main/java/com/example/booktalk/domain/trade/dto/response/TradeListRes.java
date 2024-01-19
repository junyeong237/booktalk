package com.example.booktalk.domain.trade.dto.response;

public record TradeListRes(Long id, String sellerName, String productName, String buyerName,
                           Long score, Long productId) {

}
