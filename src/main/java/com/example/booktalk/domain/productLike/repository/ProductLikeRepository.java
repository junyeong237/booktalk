package com.example.booktalk.domain.productLike.repository;

import com.example.booktalk.domain.product.entity.Product;
import com.example.booktalk.domain.productLike.entity.ProductLike;
import com.example.booktalk.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductLikeRepository extends JpaRepository<ProductLike,Long> {
    Optional<ProductLike> findByProductAndUser( Product product,User user);

    List<ProductLike> findByUser_IdAndIsProductLikedTrue(Long userId);
}
