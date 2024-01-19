package com.example.booktalk.domain.review.dto.response;

import com.example.booktalk.domain.comment.dto.response.CommentGetListRes;

import java.time.LocalDateTime;
import java.util.List;

public record ReviewGetRes(
    Long reviewId,
    Long productId,
    String title,
    String content,
    String nickname,
    Integer reviewLike,
    LocalDateTime createdAt,
    LocalDateTime modifiedAt,
    String reviewImagePathUrl,
    List<CommentGetListRes> commentList
) {
}
