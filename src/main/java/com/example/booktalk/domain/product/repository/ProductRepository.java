package com.example.booktalk.domain.product.repository;


import com.example.booktalk.domain.product.entity.Product;
import com.example.booktalk.domain.product.exception.NotFoundProductException;
import com.example.booktalk.domain.product.exception.ProductErrorCode;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    List<Product> findAllByDeletedFalse(Sort sort);

    List<Product> findTop3ByOrderByProductLikeCntDesc();

    default Product findProductByIdWithThrow(Long id) {
        return findById(id).orElseThrow(() ->
            new NotFoundProductException(ProductErrorCode.NOT_FOUND_PRODUCT));
    }


}
