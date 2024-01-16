package com.example.booktalk.domain.productcategory.repository;


import com.example.booktalk.domain.product.entity.Product;
import com.example.booktalk.domain.productcategory.entity.ProductCategory;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    Boolean existsByCategory_Id(Long categoryId);

    List<ProductCategory> findAllByProductAndCategory_NameIn(Product product,
        List<String> categories);
}
