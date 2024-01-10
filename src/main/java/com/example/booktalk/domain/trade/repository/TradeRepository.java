package com.example.booktalk.domain.trade.repository;

import com.example.booktalk.domain.trade.entity.Trade;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeRepository extends JpaRepository<Trade, Long> {


}
