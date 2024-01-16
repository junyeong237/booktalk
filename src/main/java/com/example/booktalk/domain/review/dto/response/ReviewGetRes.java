package com.example.booktalk.domain.review.dto.response;

import com.example.booktalk.domain.comment.dto.response.CommentGetListRes;

import java.util.List;

public record ReviewGetRes(
    Long reviewId,
    String title,
    String content,
    String nickname,
    Integer reviewLike,
    List<CommentGetListRes> commentList
) {
}
