package com.example.booktalk.domain.product.repository;


import com.example.booktalk.domain.product.entity.Product;
import java.util.List;
import org.springframework.data.domain.Sort;


public interface ProductRepositoryCustom {

    List<Product> getPostListByName(Sort sort, String search);

    List<Product> getProductListByTag(Sort sort, String tag);
}
