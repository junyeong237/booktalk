package com.example.booktalk.domain.trade.dto.response;

public record TradeCreateRes(Long id, String seller, String productName, String buyer,
                             Long score) {

}
