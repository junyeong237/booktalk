package com.example.booktalk.domain.admin.service;

import com.example.booktalk.domain.product.dto.response.ProductDeleteRes;
import com.example.booktalk.domain.product.entity.Product;
import com.example.booktalk.domain.product.exception.NotPermissionAuthority;
import com.example.booktalk.domain.product.exception.ProductErrorCode;
import com.example.booktalk.domain.product.repository.ProductRepository;
import com.example.booktalk.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminProductService {
    private final ProductRepository productRepository;
    public ProductDeleteRes adminDeleteProduct(Long productId) {
        Product product = productRepository.findProductByIdWithThrow(productId);

        product.deleted();

        return new ProductDeleteRes("삭제가 완료되었습니다.");

    }
}
