package com.example.booktalk.domain.product.repository;


import com.example.booktalk.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ProductRepositoryCustom {

    Page<Product> getPostListByName(Pageable pageable, String search);

    Page<Product> getProductListByTag(Pageable pageable, String tag);
}
