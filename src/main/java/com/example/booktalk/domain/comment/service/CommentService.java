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
import com.example.booktalk.domain.comment.exception.NotPermissionCommentAuthorityException;
import com.example.booktalk.domain.comment.repository.CommentRepository;
import com.example.booktalk.domain.review.entity.Review;
import com.example.booktalk.domain.review.repository.ReviewRepository;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.entity.UserRoleType;
import com.example.booktalk.domain.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public CommentCreateRes createComment(CommentCreateReq req, Long userId) {

        User user = userRepository.findUserByIdWithThrow(userId);
        Review review = reviewRepository.findReviewByIdWithThrow(req.reviewId());

        if (req.content().isEmpty()) {
            throw new EmptyContentException(CommentErrorCode.EMPTY_CONTENT);
        }

        Comment comment = Comment.builder()
            .content(req.content())
            .review(review)
            .user(user)
            .build();

        Comment result = commentRepository.save(comment);

        return new CommentCreateRes(result.getId(), result.getContent(),
            result.getUser().getNickname());
    }

    @Transactional(readOnly = true)
    public List<CommentGetListRes> getCommentList(Long reviewId) {

        Review review = reviewRepository.findReviewByIdWithThrow(reviewId);
        List<Comment> commentList = commentRepository.findAllByReviewOrderByCreatedAtDesc(review);

        return commentList.stream()
            .map(comment -> new CommentGetListRes(comment.getId(),
                comment.getContent(), comment.getUser().getNickname()))
            .toList();
    }


    public CommentUpdateRes updateComment(Long commentId, CommentUpdateReq req, Long userId) {

        User user = userRepository.findUserByIdWithThrow(userId);
        Comment comment = commentRepository.findCommentByIdWithThrow(commentId);
        validateCommentUser(user, comment);

        comment.update(req);

        return new CommentUpdateRes(comment.getId(), comment.getContent());
    }

    public CommentDeleteRes deleteComment(Long commentId, Long userId) {

        User user = userRepository.findUserByIdWithThrow(userId);
        Comment comment = commentRepository.findCommentByIdWithThrow(commentId);
        validateCommentUser(user, comment);

        commentRepository.delete(comment);

        return new CommentDeleteRes("댓글이 삭제되었습니다.");
    }


    private void validateCommentUser(User user, Comment comment) {
        if (!user.getId().equals(comment.getUser().getId())
            && !user.getRole().equals(UserRoleType.ADMIN)) {
            throw new NotPermissionCommentAuthorityException(
                CommentErrorCode.NOT_PERMISSION_COMMENT_AUTHORITY);
        }
    }

}
