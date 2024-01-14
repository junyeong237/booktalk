package com.example.booktalk.domain.admin.service;

import com.example.booktalk.domain.trade.dto.response.TradeAllListRes;
import com.example.booktalk.domain.trade.repository.TradeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminTradeService {

    private final TradeRepository tradeRepository;

    @Transactional(readOnly = true)
    public List<TradeAllListRes> getTradeAllList() {

        return tradeRepository.findAll().stream()
            .map(
                trade -> {
                    return new TradeAllListRes(trade.getId(),
                        trade.getSeller().getNickname(), trade.getProduct().getName(),
                        trade.getBuyer().getNickname(), trade.getScore());
                }
            )
            .toList();

    }


}
