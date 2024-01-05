package com.example.booktalk.domain.product.repository;


import com.example.booktalk.domain.product.entity.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAll();

}
