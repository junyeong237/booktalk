package com.example.booktalk.domain.imageFile.repository;


import com.example.booktalk.domain.imageFile.entity.ImageFile;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageFileRepository extends JpaRepository<ImageFile, Long> {

    List<ImageFile> findByProductId(Long productId);
    
}