package com.example.booktalk.domain.trade.repository;

import com.example.booktalk.domain.trade.entity.Trade;
import com.example.booktalk.domain.trade.exception.NotFoundTradeException;
import com.example.booktalk.domain.trade.exception.TradeErrorCode;
import com.example.booktalk.domain.user.entity.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade, Long> {

    List<Trade> findAllByBuyer(User buyer);

    default Trade findTradeByIdWithThrow(Long id) {
        return findById(id).orElseThrow(() ->
            new NotFoundTradeException(TradeErrorCode.NOT_FOUND_TRADE));
    }

}
