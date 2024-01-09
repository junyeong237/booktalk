package com.example.booktalk.domain.productLike.service;

import com.example.booktalk.domain.product.entity.Product;
import com.example.booktalk.domain.product.exception.NotFoundProductException;
import com.example.booktalk.domain.product.exception.ProductErrorCode;
import com.example.booktalk.domain.product.repository.ProductRepository;
import com.example.booktalk.domain.productLike.dto.response.ProductLikeRes;
import com.example.booktalk.domain.productLike.entity.ProductLike;
import com.example.booktalk.domain.productLike.repository.ProductLikeRepository;
import com.example.booktalk.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductLikeService {
    private final ProductRepository productRepository;
    private final ProductLikeRepository productLikeRepository;
    public ProductLikeRes switchLikeProduct(Long productId, User user) {

        // 댓글 서비스를 통해 댓글을 조회
        Product product = findProduct(productId);

        // 댓글에 대한 좋아요 정보를 찾거나, 없으면 생성하여 가져옴
        ProductLike productLike = productLikeRepository.findByProductAndUser(product,user)
                .orElseGet(() -> saveProductLike(product,user));

        // 좋아요 정보를 전환하고, 해당 댓글의 좋아요 수를 갱신
        Boolean updated = productLike.clickProdctLike();
//        product.updateProductLikeCnt(updated);

        // LikeResponseDto 생성자를 사용하여 좋아요 여부를 포함한 응답 객체 생성
        return new ProductLikeRes(productLike.getIsProductLiked());
    }

    @Transactional
    public ProductLike saveProductLike(Product product, User user) {

        // 게시글 정보를 기반으로 좋아요 엔티티를 생성
        ProductLike productLike = ProductLike.builder()
                .product(product)
                .user(user)
                .build();

        // 생성된 좋아요 엔티티를 저장하고 반환
        return productLikeRepository.save(productLike);
    }

    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundProductException(ProductErrorCode.NOT_FOUND_PRODUCT));
    }
}
