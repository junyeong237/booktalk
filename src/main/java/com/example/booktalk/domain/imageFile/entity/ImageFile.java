package com.example.booktalk.domain.imageFile.entity;

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

    private String image;


    @Builder
    private ImageFile(String image){
        this.image=image;
    };

    public void updateImage(String image){
        this.image=image;
    }
}
