package com.example.booktalk.domain.productLike.service;

import com.example.booktalk.domain.product.entity.Product;
import com.example.booktalk.domain.product.exception.NotFoundProductException;
import com.example.booktalk.domain.product.exception.ProductErrorCode;
import com.example.booktalk.domain.product.repository.ProductRepository;
import com.example.booktalk.domain.productLike.dto.response.ProductLikeRes;
import com.example.booktalk.domain.productLike.entity.ProductLike;
import com.example.booktalk.domain.productLike.repository.ProductLikeRepository;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductLikeService {
    private final ProductRepository productRepository;
    private final ProductLikeRepository productLikeRepository;
    private final UserRepository userRepository;
    @Transactional
    public ProductLikeRes switchLikeProduct(Long productId, Long userId) {

        User user=findUser(userId);

        Product product = findProduct(productId);

        ProductLike productLike = productLikeRepository.findByProductAndUser(product,user)
                .orElseGet(() -> saveProductLike(product,user));

        Boolean updated = productLike.clickProdctLike();
        product.updateProductLikeCnt(updated);

        return new ProductLikeRes(productLike.getIsProductLiked());
    }

    @Transactional
    public ProductLike saveProductLike(Product product, User user) {

        ProductLike productLike = ProductLike.builder()
                .product(product)
                .user(user)
                .build();

        return productLikeRepository.save(productLike);
    }

    private Product findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundProductException(ProductErrorCode.NOT_FOUND_PRODUCT));
    }

    private User findUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당하는유저가 없습니다."));
    }
}
