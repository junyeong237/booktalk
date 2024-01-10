package com.example.booktalk.domain.trade.dto.response;

import com.example.booktalk.domain.product.dto.response.ProductRes;
import com.example.booktalk.domain.user.dto.response.UserRes;

public record TradeGetRes(Long id, UserRes user, ProductRes product, Long score) {

}
