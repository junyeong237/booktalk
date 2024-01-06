package com.example.booktalk.domain.reviewlike.service;

import com.example.booktalk.domain.reviewlike.repository.ReviewLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewLikeService {

    private final ReviewLikeRepository reviewLikeRepository;


}
