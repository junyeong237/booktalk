package com.example.booktalk.domain.admin.controller;

import com.example.booktalk.domain.admin.service.AdminTradeService;
import com.example.booktalk.domain.trade.dto.response.TradeAllListRes;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/admin/trades")
@RequiredArgsConstructor
public class AdminTradeController {

    private final AdminTradeService adminTradeService;

    @GetMapping
    public List<TradeAllListRes> getUser() {
        List<TradeAllListRes> res = adminTradeService.getTradeAllList();
        return res;
    }

}
