package com.example.booktalk.domain.review.dto.response;

import com.example.booktalk.domain.user.entity.User;
import lombok.Builder;

@Builder
public record ReviewCreateRes (
        Long reviewId,
        String title,
        String content,
        User user
) {
}
