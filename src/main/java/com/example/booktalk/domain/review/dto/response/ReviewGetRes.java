package com.example.booktalk.domain.review.dto.response;

import com.example.booktalk.domain.comment.dto.response.CommentGetListRes;
import com.example.booktalk.domain.comment.entity.Comment;
import com.example.booktalk.domain.user.entity.User;
import lombok.Builder;

import java.util.List;

@Builder
public record ReviewGetRes (
        Long reviewId,
        String title,
        String content,
        User user,
        List<Comment> commentList
) {
}
