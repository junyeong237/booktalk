package com.example.booktalk.domain.imageFile.repository;


import com.example.booktalk.domain.imageFile.entity.ImageFile;
import com.example.booktalk.domain.imageFile.exception.ImageFileErrorCode;
import com.example.booktalk.domain.imageFile.exception.NotFoundImageFileException;
import com.example.booktalk.domain.review.entity.Review;
import com.example.booktalk.domain.review.exception.NotFoundReviewException;
import com.example.booktalk.domain.review.exception.ReviewErrorCode;
import com.example.booktalk.domain.user.exception.NotFoundUserException;
import com.example.booktalk.domain.user.exception.UserErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ImageFileRepository extends JpaRepository<ImageFile,Long> {
    List<ImageFile> findByProductId(Long productId);
    ImageFile findByUserNickname(String nickname);

}
