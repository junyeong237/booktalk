package com.example.booktalk.domain.trade.service;

import com.example.booktalk.domain.product.entity.Product;
import com.example.booktalk.domain.product.repository.ProductRepository;
import com.example.booktalk.domain.trade.dto.request.TradeCreateReq;
import com.example.booktalk.domain.trade.dto.response.TradeCreateRes;
import com.example.booktalk.domain.trade.dto.response.TradeGetRes;
import com.example.booktalk.domain.trade.dto.response.TradeListRes;
import com.example.booktalk.domain.trade.entity.Trade;
import com.example.booktalk.domain.trade.exception.ForbiddenReadTradeListException;
import com.example.booktalk.domain.trade.exception.InvalidScroeInputException;
import com.example.booktalk.domain.trade.exception.NotPermissionRegisterTrade;
import com.example.booktalk.domain.trade.exception.TradeErrorCode;
import com.example.booktalk.domain.trade.repository.TradeRepository;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.repository.UserRepository;
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
        validateBuyerAndSeller(userId, product);

        User seller = product.getUser();

        validateScore(req.score());

        Trade trade = Trade.builder()
            .buyer(buyer)
            .product(product)
            .score(req.score())
            .seller(seller)
            .build();

        tradeRepository.save(trade);

        Double averageScore = calculateAverageScore(tradeRepository.findBySellerId(seller.getId()));
        seller.setScore(averageScore);

        return new TradeCreateRes(trade.getId(), buyer.getNickname(), product.getName(),
            seller.getNickname(),
            trade.getScore());

    }


    @Transactional(readOnly = true)
    public TradeGetRes getTrade(Long userId, Long tradeId) { // 거래내역 단건 조회

        Trade trade = tradeRepository.findTradeByIdWithThrow(tradeId);
        User buyer = trade.getBuyer();

        validateOwnTrade(buyer.getId(), trade);

        Product soldProduct = trade.getProduct();
        User seller = soldProduct.getUser();

        return new TradeGetRes(trade.getId(), buyer.getNickname(), soldProduct.getName(),
            seller.getNickname(), trade.getScore());

    }


    @Transactional(readOnly = true)
    public List<TradeListRes> getTradeList(Long userId) { //본인의 거래 내역 전체 조회

        User user = userRepository.findUserByIdWithThrow(userId);

        List<Trade> tradeLists = tradeRepository.getTradeListByUser(user);

        return tradeLists.stream()
            .map(trade -> new TradeListRes(trade.getId(), trade.getSeller().getNickname(),
                trade.getProduct().getName(), trade.getBuyer().getNickname(),
                trade.getScore(), trade.getProduct().getId()))
            .toList();

    }


    private void validateBuyerAndSeller(Long buyerId, Product product) {

        if (buyerId.equals(product.getUser().getId())) {
            throw new NotPermissionRegisterTrade(TradeErrorCode.NOT_PERMISSION_REGITSTER_TRADE);
        }

    }

    private void validateOwnTrade(Long userId, Trade trade) {
        if (!trade.getBuyer().getId().equals(userId)) { // 성능확인
            throw new ForbiddenReadTradeListException(TradeErrorCode.FORBIDDEN_READ_TRADE_LIST);
        }
    }

    private void validateScore(Long score) {
        if (score > 10 || score < 1) {
            throw new InvalidScroeInputException(TradeErrorCode.INVALIE_SCORE_INPUT);
        }
    }

    private Double calculateAverageScore(List<Trade> tradeList) {
        if (!tradeList.isEmpty()) {
            Double sum = 7.0;
            for (Trade trade : tradeList) {
                sum += trade.getScore();
            }
            return Math.round((sum / (tradeList.size() + 1)) * 10.0) / 10.0;
        } else {
            return 0.0;
        }
    }


}
