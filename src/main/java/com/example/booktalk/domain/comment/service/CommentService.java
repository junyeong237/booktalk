package com.example.booktalk.domain.comment.service;

import com.example.booktalk.domain.comment.dto.request.CommentCreateReq;
import com.example.booktalk.domain.comment.dto.request.CommentUpdateReq;
import com.example.booktalk.domain.comment.dto.response.CommentCreateRes;
import com.example.booktalk.domain.comment.dto.response.CommentDeleteRes;
import com.example.booktalk.domain.comment.dto.response.CommentGetListRes;
import com.example.booktalk.domain.comment.dto.response.CommentUpdateRes;
import com.example.booktalk.domain.comment.entity.Comment;
import com.example.booktalk.domain.comment.exception.CommentErrorCode;
import com.example.booktalk.domain.comment.exception.EmptyContentException;
import com.example.booktalk.domain.comment.exception.NotFoundCommentException;
import com.example.booktalk.domain.comment.exception.NotPermissionCommentAuthorityException;
import com.example.booktalk.domain.comment.repository.CommentRepository;
import com.example.booktalk.domain.review.entity.Review;
import com.example.booktalk.domain.review.exception.NotFoundReviewException;
import com.example.booktalk.domain.review.exception.ReviewErrorCode;
import com.example.booktalk.domain.review.repository.ReviewRepository;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public CommentCreateRes createComment(CommentCreateReq req, Long userId) {

        User user = findUser(userId);
        Review review = findReview(req.reviewId());

        if(req.content().isEmpty()) {
            throw new EmptyContentException(CommentErrorCode.EMPTY_CONTENT);
        }

        Comment comment = Comment.builder()
                .content(req.content())
                .review(review)
                .user(user)
                .build();

        commentRepository.save(comment);

        return CommentCreateRes.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .nickname(comment.getUser().getNickname())
                .build();
    }

    public List<CommentGetListRes> getCommentList(Long reviewId) {

        Review review = findReview(reviewId);
        List<Comment> commentList = commentRepository.findAllByReviewOrderByCreatedAtDesc(review);

        return commentList.stream().map(comment -> CommentGetListRes.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .nickname(comment.getUser().getNickname())
                .build()).toList();
    }

    @Transactional
    public CommentUpdateRes updateComment(Long commentId, CommentUpdateReq req, Long userId) {

        User user = findUser(userId);
        Comment comment = findComment(commentId);
        validateCommentUser(user, comment);

        comment.update(req);

        return CommentUpdateRes.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .build();
    }

    public CommentDeleteRes deleteComment(Long commentId, Long userId) {

        User user = findUser(userId);
        Comment comment = findComment(commentId);
        validateCommentUser(user, comment);

        commentRepository.delete(comment);

        return CommentDeleteRes.builder()
                .msg("댓글이 삭제되었습니다.")
                .build();
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 유저가 없습니다."));
    }

    private Review findReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundReviewException(ReviewErrorCode.NOT_FOUND_REVIEW));
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundCommentException(CommentErrorCode.NOT_FOUND_COMMENT));
    }

    private void validateCommentUser(User user, Comment comment) {
        if(!user.getId().equals(comment.getUser().getId())) {
            throw new NotPermissionCommentAuthorityException(CommentErrorCode.NOT_PERMISSION_COMMENT_AUTHORITY);
        }
    }

}
