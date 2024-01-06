package com.example.booktalk.domain.comment.service;

import com.example.booktalk.domain.comment.dto.request.CommentUpdateReq;
import com.example.booktalk.domain.comment.dto.response.CommentGetListRes;
import com.example.booktalk.domain.comment.dto.response.CommentUpdateRes;
import com.example.booktalk.domain.comment.repository.CommentRepository;
import com.example.booktalk.domain.comment.dto.request.CommentCreateReq;
import com.example.booktalk.domain.comment.dto.response.CommentCreateRes;
import com.example.booktalk.domain.comment.dto.response.CommentDeleteRes;
import com.example.booktalk.domain.comment.entity.Comment;
import com.example.booktalk.domain.review.entity.Review;
import com.example.booktalk.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;

    public CommentCreateRes createComment(CommentCreateReq req) {
        Review review = findReview(req.reviewId());

        Comment comment = Comment.builder()
                .content(req.content())
                .review(review)
                .build();

        commentRepository.save(comment);

        return CommentCreateRes.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .build();
    }

    public List<CommentGetListRes> getCommentList(Long reviewId) {
        Review review = findReview(reviewId);
        List<Comment> commentList = commentRepository.findAllByReview(review);

        return commentList.stream().map(comment -> CommentGetListRes.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .build()).toList();
    }

    @Transactional
    public CommentUpdateRes updateComment(Long commentId, CommentUpdateReq req) {
        Comment comment = findComment(commentId);

        comment.update(req);

        return CommentUpdateRes.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .build();
    }

    public CommentDeleteRes deleteComment(Long commentId) {
        Comment comment = findComment(commentId);

        commentRepository.delete(comment);

        return CommentDeleteRes.builder()
                .msg("삭제되었습니다.")
                .build();
    }

    private Review findReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 id의 게시글이 없습니다."));
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 id의 댓글이 없습니다."));
    }

}
