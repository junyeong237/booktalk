package com.example.booktalk.domain.review.dto.response;

import com.example.booktalk.domain.comment.dto.response.CommentGetListRes;
import com.example.booktalk.domain.review.entity.Review;
import java.util.List;

public record ReviewGetRes(
    Long reviewId,
    String title,
    String content,
    String nickname,
    List<CommentGetListRes> commentList
) {

    public ReviewGetRes(Review review) {
        this(review.getId(), review.getTitle(), review.getContent(),
            review.getUser().getNickname(), review.getCommentList().stream()
                .map(CommentGetListRes::new).toList());
    }
}
