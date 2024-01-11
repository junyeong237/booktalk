package com.example.booktalk.domain.product.repository;


import com.example.booktalk.domain.product.entity.Product;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepositoryCustom {

    public List<Product> getPostListByName(Sort sort, String search);

    public List<Product> getProductListByTag(Sort sort, String tag);
}
