package com.example.booktalk.domain.trade.service;

import com.example.booktalk.domain.product.dto.response.ProductRes;
import com.example.booktalk.domain.product.entity.Product;
import com.example.booktalk.domain.product.repository.ProductRepository;
import com.example.booktalk.domain.trade.dto.request.TradeCreateReq;
import com.example.booktalk.domain.trade.dto.response.TradeCreateRes;
import com.example.booktalk.domain.trade.dto.response.TradeGetRes;
import com.example.booktalk.domain.trade.dto.response.TradeListRes;
import com.example.booktalk.domain.trade.entity.Trade;
import com.example.booktalk.domain.trade.repository.TradeRepository;
import com.example.booktalk.domain.user.dto.response.UserRes;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.admin.controller.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class TradeService {

    private final TradeRepository tradeRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public TradeCreateRes createTrade(Long userId, TradeCreateReq req) {
        User buyer = userRepository.findUserByIdWithThrow(userId);
        Product product = productRepository.findProductByIdWithThrow(req.productId());

        //구매자랑 판매자 아이디랑 같은지 확인
        validateBuyerAndSeller(userId, req.sellerId());

        User seller = product.getUser();

        Trade trade = Trade.builder()
            .buyer(buyer)
            .product(product)
            .score(req.score())
            .seller(seller)
            .build();

        tradeRepository.save(trade);

        seller.averageScore();

        UserRes userRes = new UserRes(seller.getId(), seller.getNickname());

        //TODO : 판매자 평점 업데이트
        ProductRes productRes = new ProductRes(
            product.getId(), product.getName(), product.getPrice(), userRes
        );
        return new TradeCreateRes(trade.getId(), productRes,
            trade.getScore());

    }


    @Transactional(readOnly = true)
    public TradeGetRes getTrade(Long userId, Long tradeId) { // 거래내역 단건 조회

        Trade trade = tradeRepository.findTradeByIdWithThrow(tradeId);
        User buyer = trade.getBuyer();

        validateOwnTrade(buyer.getId(), trade);

        Product soldProduct = trade.getProduct();
        User seller = soldProduct.getUser();
        UserRes userResBuyer = new UserRes(buyer.getId(), buyer.getNickname());
        UserRes userResSeller = new UserRes(seller.getId(), seller.getNickname());

        //TODO : 판매자 평점 업데이트
        ProductRes productRes = new ProductRes(
            soldProduct.getId(), soldProduct.getName(), soldProduct.getPrice(), userResSeller
        );

        return new TradeGetRes(trade.getId(), userResBuyer, productRes, trade.getScore());

    }


    @Transactional(readOnly = true)
    public List<TradeListRes> getTradeList(Long userId) { //본인의 거래 내역 전체 조회

        User buyer = userRepository.findUserByIdWithThrow(userId);

        List<Trade> tradeList = tradeRepository.findAllByBuyer(buyer);

        List<TradeListRes> tradeListRes = tradeList.stream().map(
            trade -> {
                Product soldProduct = trade.getProduct();
                User seller = soldProduct.getUser();
                UserRes userResBuyer = new UserRes(buyer.getId(), buyer.getNickname());
                UserRes userResSeller = new UserRes(seller.getId(), seller.getNickname());
                ProductRes productRes = new ProductRes(
                    soldProduct.getId(), soldProduct.getName(), soldProduct.getPrice(),
                    userResSeller
                );

                return new TradeListRes(trade.getId(), userResBuyer, productRes, trade.getScore());
            }

        ).toList();

        return tradeListRes;

    }


    private void validateBuyerAndSeller(Long buyerId, Long sellerId) {

        if (buyerId.equals(sellerId)) {
            throw new IllegalArgumentException("판매자는 본인의 상품을 구매할 수 없습니다.");
        }

    }

    private void validateOwnTrade(Long userId, Trade trade) {
        if (!trade.getBuyer().getId().equals(userId)) { // 성능확인
            throw new IllegalArgumentException("자신의 거래내역이 아니라 확인 할 수 없습니다.");
        }
    }

}
