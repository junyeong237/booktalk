package com.example.booktalk.domain.trade.dto.response;

public record TradeGetRes(Long id, String buyerName, String productName, String sellerName,
                          Long score) {

}
