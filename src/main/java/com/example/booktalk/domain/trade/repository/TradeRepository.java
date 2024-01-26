package com.example.booktalk.domain.trade.repository;

import com.example.booktalk.domain.trade.entity.Trade;
import com.example.booktalk.domain.trade.exception.NotFoundTradeException;
import com.example.booktalk.domain.trade.exception.TradeErrorCode;
import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade, Long>, TradeRepositoryCustom {

    @CacheEvict(value = "user", key = "#sellerId")
    List<Trade> findBySellerId(Long sellerId);

    default Trade findTradeByIdWithThrow(Long id) {
        return findById(id).orElseThrow(() ->
            new NotFoundTradeException(TradeErrorCode.NOT_FOUND_TRADE));
    }

}
