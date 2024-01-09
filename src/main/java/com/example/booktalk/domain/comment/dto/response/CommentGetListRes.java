package com.example.booktalk.domain.comment.dto.response;

import com.example.booktalk.domain.user.entity.User;
import lombok.Builder;

@Builder
public record CommentGetListRes (
        Long commentId,
        String content,
        String nickname
) {
}
