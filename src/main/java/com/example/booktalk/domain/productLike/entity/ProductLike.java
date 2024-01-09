package com.example.booktalk.domain.productLike.entity;

import com.example.booktalk.domain.product.entity.Product;
import com.example.booktalk.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductLike {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private Boolean isProductLiked;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id")
        private User user;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "product_id")
        private Product product;

        @Builder
        private ProductLike(Long id, Boolean isProductLiked, User user, Product product) {
            this.id = id;
            this.isProductLiked = isProductLiked;
            this.user = user;
            this.product = product;
        }

        public Boolean clickProdctLike() {
            return this.isProductLiked = !isProductLiked;
        }
}

