package com.example.booktalk.domain.imageFile.repository;


import com.example.booktalk.domain.imageFile.entity.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageFileRepository extends JpaRepository<ImageFile,Long> {
    List<ImageFile> findByProductId(Long productId);

    Optional<ImageFile> findByProductIdAndId(Long productId, Long imageId);

}
