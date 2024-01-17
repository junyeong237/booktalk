package com.example.booktalk.domain.imageFile.entity;

import com.example.booktalk.domain.product.entity.Product;
import com.example.booktalk.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name="TB_IMAGE")
public class ImageFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String imagePathUrl;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;


    @Builder
    private ImageFile(String imagePathUrl,User user,Product product){
        this.imagePathUrl=imagePathUrl;
        this.user=user;
        this.product=product;
    };
}
