package com.example.booktalk.domain.trade.controller;

import com.example.booktalk.domain.trade.dto.request.TradeCreateReq;
import com.example.booktalk.domain.trade.dto.response.TradeCreateRes;
import com.example.booktalk.domain.trade.dto.response.TradeGetRes;
import com.example.booktalk.domain.trade.dto.response.TradeListRes;
import com.example.booktalk.domain.trade.service.TradeService;
import com.example.booktalk.global.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/trades")
@RequiredArgsConstructor
public class TradeController {

    private final TradeService tradeService;


    @PostMapping
    public TradeCreateRes createTrade(@AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody TradeCreateReq req) {

        TradeCreateRes res = tradeService.createTrade(userDetails.getUser().getId(), req);

        return res;

    }

    @GetMapping("/{tradeId}")
    public TradeGetRes getTrade(@AuthenticationPrincipal UserDetailsImpl userDetails,
        @PathVariable Long tradeId) {

        TradeGetRes res = tradeService.getTrade(userDetails.getUser().getId(), tradeId);

        return res;

    }

    @GetMapping
    public List<TradeListRes> getTrade(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<TradeListRes> res = tradeService.getTradeList(userDetails.getUser().getId());

        return res;

    }


}
