package com.example.booktalk.domain.productLike.service;

import com.example.booktalk.domain.product.entity.Product;
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
    public ProductLikeRes switchProductLike(Long productId, Long userId) {

        User user=userRepository.findUserByIdWithThrow(userId);

        Product product = productRepository.findProductByIdWithThrow(productId);

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


}
