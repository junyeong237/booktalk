package com.example.booktalk.domain.product.entity;

import com.example.booktalk.domain.common.BaseEntity;
import com.example.booktalk.domain.product.dto.request.ProductUpdateReq;
import com.example.booktalk.domain.productcategory.entity.ProductCategory;
import com.example.booktalk.domain.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "TB_PRODUCT")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private Long quantity;

    @Column(nullable = false)
    private Long productLikeCnt;

    @Enumerated(EnumType.STRING)
    private Region region;

    @Column(nullable = false)
    private Boolean finished;

    @Column(nullable = false)
    private String content;


    private Boolean deleted;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private final List<ProductCategory> productCategoryList = new ArrayList<>();

    @Builder
    private Product(String name, Long price, Long quantity, Region region, String content,
        User user) {
        this.productLikeCnt = 0L;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.region = region;
        this.content = content;
        this.user = user;
        this.finished = false;
        this.deleted = false;
    }

    public void update(ProductUpdateReq req) {
        this.name = req.name();
        this.quantity = req.quantity();
        this.price = req.price();
        this.region = req.region();
        this.content = req.content();
        this.finished = req.finished();
    }

    public void finish() { //거래 상태를 완료로 변경
        this.finished = true;
    }

    public void deleted() {
        this.deleted = true;
    }

    public void addProductCategory(ProductCategory productCategory) {
        this.productCategoryList.add(productCategory);
        productCategory.setProduct(this);
    }

    public void updateProductLikeCnt(Boolean updated) {
        if (updated) {
            this.productLikeCnt++;
        } else {
            this.productLikeCnt--;
        }
    }
}
