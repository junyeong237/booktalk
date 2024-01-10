package com.example.booktalk.domain.trade.dto.response;

import com.example.booktalk.domain.product.dto.response.ProductRes;

public record TradeCreateRes(Long id, ProductRes productRes, Long score) {

}
