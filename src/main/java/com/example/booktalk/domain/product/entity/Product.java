package com.example.booktalk.domain.product.entity;

import com.example.booktalk.domain.common.BaseEntity;
import com.example.booktalk.domain.product.dto.request.ProductRegisterReq;
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
import jakarta.persistence.Table;
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
    @Enumerated(EnumType.STRING)
    private List<Region> regions;

    @Column(nullable = false)
    private Boolean finished;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    private Product(String name, Long price, Long quantity, List<Region> regions, User user) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
        this.regions = regions;
        this.user = user;
        this.finished = false;
    }

    public void update(ProductRegisterReq req) {
        this.name = req.name();
        this.quantity = req.quantity();
        this.price = req.price();
        this.regions = req.regions();
        this.finished = req.finished();
    }

    public void finish() { //거래 상태를 완료로 변경
        this.finished = true;
    }

}
