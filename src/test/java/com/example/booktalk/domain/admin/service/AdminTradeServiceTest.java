package com.example.booktalk.domain.admin.service;

import com.example.booktalk.domain.admin.service.AdminTradeService;
import com.example.booktalk.domain.product.entity.Product;
import com.example.booktalk.domain.trade.dto.response.TradeAllListRes;
import com.example.booktalk.domain.trade.entity.Trade;
import com.example.booktalk.domain.trade.repository.TradeRepository;
import com.example.booktalk.domain.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class AdminTradeServiceTest {

    @Mock
    private TradeRepository tradeRepository;

    @InjectMocks
    private AdminTradeService adminTradeService;

    @BeforeEach
    public void setUp() {
        // Mockito 초기화
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("거래 내역 조회")
    public void testGetTradeAllList() {
        // Given
        // 필요한 객체들을 생성하고 초기화
        Product product = Product.builder().name("책").build();
        User seller = User.builder().randomNickname("판매자").build();
        User buyer = User.builder().randomNickname("구매자").build();
        Trade trade1 = Trade.builder().score(5L).seller(seller).buyer(buyer).product(product).build();
        ReflectionTestUtils.setField(trade1, "id", 1L);
        when(tradeRepository.findAll()).thenReturn(Arrays.asList(trade1));

        // When
        // 테스트 대상 메서드 호출
        List<TradeAllListRes> result = adminTradeService.getTradeAllList();

        // Then
        // 결과 검증
        assertEquals(1, result.size());
        TradeAllListRes tradeAllListRes = result.get(0);
        assertEquals(1L, tradeAllListRes.id());
        assertEquals("판매자", tradeAllListRes.sellerName());
        // 필요에 따라 다른 필드들에 대한 검증 수행
    }
}

