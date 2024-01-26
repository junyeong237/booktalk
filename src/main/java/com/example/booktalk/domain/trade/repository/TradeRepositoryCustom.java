package com.example.booktalk.domain.trade.repository;

import com.example.booktalk.domain.trade.entity.Trade;
import com.example.booktalk.domain.user.entity.User;

import java.util.List;

public interface TradeRepositoryCustom {

    List<Trade> getTradeListByUser(User user);
}
