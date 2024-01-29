package com.example.booktalk.domain.product.repository;


import com.example.booktalk.domain.product.entity.Product;
import com.example.booktalk.domain.product.exception.NotFoundProductException;
import com.example.booktalk.domain.product.exception.ProductErrorCode;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductRepository extends JpaRepository<Product, Long>, ProductRepositoryCustom {

    Page<Product> findAllByDeletedFalse(Pageable pageable);

    Page<Product> findProductsByUserIdAndDeletedFalse(Long userId, Pageable pageable);

    List<Product> findTop3ByDeletedFalseOrderByProductLikeCntDesc();

    List<Product> findProductsByUserId(Long userId);

    default Product findProductByIdWithThrow(Long id) {
        return findById(id).orElseThrow(() ->
            new NotFoundProductException(ProductErrorCode.NOT_FOUND_PRODUCT));
    }

}
