package com.example.booktalk.domain.trade.repository;

import com.example.booktalk.domain.trade.entity.QTrade;
import com.example.booktalk.domain.trade.entity.Trade;
import com.example.booktalk.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class TradeRepositoryCustomImpl implements TradeRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    QTrade trade = QTrade.trade;

    @Override
    public List<Trade> getTradeListByUser(User user) {
        return jpaQueryFactory
                .selectFrom(trade)
                .where(trade.buyer.eq(user).or(trade.seller.eq(user)))
                .fetch();
    }


}
