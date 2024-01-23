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
import com.example.booktalk.domain.review.repository.ReviewRepository;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.entity.UserRoleType;
import com.example.booktalk.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @InjectMocks
    CommentService commentService;

    @Mock
    CommentRepository commentRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    ReviewRepository reviewRepository;

    User user;
    User user2;
    Review review;

    @BeforeEach
    void init() {
        user = User.builder()
                .email("email@gmail.com")
                .password("12345678")
                .randomNickname("nickname1")
                .role(UserRoleType.USER)
                .build();
        user2 = User.builder()
                .email("email2@gmail.com")
                .password("12345678")
                .randomNickname("nickname2")
                .role(UserRoleType.USER)
                .build();
        ReflectionTestUtils.setField(user, "id", 1L);
        ReflectionTestUtils.setField(user2, "id", 2L);

        review = Review.builder()
                .title("title")
                .content("content")
                .user(user)
                .build();
        ReflectionTestUtils.setField(review, "id", 1L);
    }

    @Nested
    class Comment_생성_테스트 {

        @Test
        void Comment_생성_성공() {
            //given
            given(userRepository.findUserByIdWithThrow(1L)).willReturn(user);
            given(reviewRepository.findReviewByIdWithThrow(1L)).willReturn(review);
            CommentCreateReq req = new CommentCreateReq(1L, "content");
            Comment comment = Comment.builder()
                    .user(user)
                    .review(review)
                    .content(req.content())
                    .build();
            ReflectionTestUtils.setField(comment, "id", 1L);
            given(commentRepository.save(any())).willReturn(comment);

            //when
            CommentCreateRes result = commentService.createComment(req, user.getId());

            //then
            assertThat(result.commentId()).isEqualTo(1L);
            assertThat(result.content()).isEqualTo("content");
        }

        @Test
        void Comment_생성_시_content가_비어있으면_에러를_반환() {
            //given
            CommentCreateReq req = new CommentCreateReq(1L, "");
            given(userRepository.findUserByIdWithThrow(1L)).willReturn(user);
            given(reviewRepository.findReviewByIdWithThrow(1L)).willReturn(review);
            Comment comment = Comment.builder()
                    .user(user)
                    .review(review)
                    .content(req.content())
                    .build();
            ReflectionTestUtils.setField(comment, "id", 1L);

            //when & then
            assertThrows(EmptyContentException.class, () -> {commentService.createComment(req, user.getId());});
        }
    }

    @Nested
    class Comment_조회_테스트 {

        @Test
        void Comment_조회_성공() {
            //given
            given(reviewRepository.findReviewByIdWithThrow(1L)).willReturn(review);
            Comment comment1 = Comment.builder()
                    .user(user2)
                    .content("commentContent1")
                    .build();
            Comment comment2 = Comment.builder()
                    .user(user2)
                    .content("commentContent2")
                    .build();
            ReflectionTestUtils.setField(comment1, "id", 1L);
            ReflectionTestUtils.setField(comment2, "id", 2L);
            List<Comment> commentList = new ArrayList<>();
            commentList.add(comment1);
            commentList.add(comment2);
            given(commentRepository.findAllByReviewOrderByCreatedAtDesc(review)).willReturn(commentList);

            //when
            List<CommentGetListRes> result = commentService.getCommentList(review.getId());

            //then
            assertThat(result.size()).isEqualTo(2);
            assertThat(result.get(0).commentId()).isEqualTo(1L);
            assertThat(result.get(0).content()).isEqualTo("commentContent1");
            assertThat(result.get(1).commentId()).isEqualTo(2L);
            assertThat(result.get(1).content()).isEqualTo("commentContent2");
        }
    }

    @Nested
    class Comment_수정_테스트 {

        @Test
        void Comment_수정_성공() {
            //given
            given(userRepository.findUserByIdWithThrow(1L)).willReturn(user);
            Comment comment = Comment.builder()
                    .user(user)
                    .review(review)
                    .content("content")
                    .build();
            ReflectionTestUtils.setField(comment, "id", 1L);
            CommentUpdateReq req = new CommentUpdateReq("update_content");
            given(commentRepository.findCommentByIdWithThrow(1L)).willReturn(comment);

            //when
            CommentUpdateRes result = commentService.updateComment(comment.getId(), req, user.getId());

            //then
            assertThat(result.commentId()).isEqualTo(1L);
            assertThat(result.content()).isEqualTo("update_content");
        }

        @Test
        void Comment_수정_시_Comment가_존재하지_않으면_에러를_반환() {
            //given
            given(userRepository.findUserByIdWithThrow(1L)).willReturn(user);
            Long nonExistingCommentId = 100L;
            CommentUpdateReq req = new CommentUpdateReq("update_content");
            given(commentRepository.findCommentByIdWithThrow(nonExistingCommentId))
                    .willThrow(new NotFoundCommentException(CommentErrorCode.NOT_FOUND_COMMENT));

            //when & then
            assertThrows(NotFoundCommentException.class, () -> commentService.updateComment(nonExistingCommentId, req, user.getId()));
        }

        @Test
        void Comment_수정_시_본인의_comment가_아니면_에러를_반환() {
            //given
            given(userRepository.findUserByIdWithThrow(2L)).willReturn(user2);
            Comment comment = Comment.builder()
                    .user(user)
                    .review(review)
                    .content("content")
                    .build();
            ReflectionTestUtils.setField(comment, "id", 1L);
            CommentUpdateReq req = new CommentUpdateReq("update_content");
            given(commentRepository.findCommentByIdWithThrow(1L)).willReturn(comment);

            //when & then
            assertThrows(NotPermissionCommentAuthorityException.class, () -> commentService.updateComment(comment.getId(), req, user2.getId()));

            verify(reviewRepository, never()).save(any());
        }
    }

    @Nested
    class Comment_삭제_테스트 {

        @Test
        void Comment_삭제_성공() {
            //given
            given(userRepository.findUserByIdWithThrow(1L)).willReturn(user);
            Comment comment = Comment.builder()
                    .user(user)
                    .review(review)
                    .content("content")
                    .build();
            ReflectionTestUtils.setField(comment, "id", 1L);
            given(commentRepository.findCommentByIdWithThrow(1L)).willReturn(comment);

            //when
            CommentDeleteRes result = commentService.deleteComment(comment.getId(), user.getId());

            //then
            assertThat(result.msg()).isEqualTo("댓글이 삭제되었습니다.");
        }

        @Test
        void Comment_삭제_시_Comment가_존재하지_않으면_에러를_반환() {
            //given
            given(userRepository.findUserByIdWithThrow(1L)).willReturn(user);
            Long nonExistingCommentId = 100L;
            given(commentRepository.findCommentByIdWithThrow(nonExistingCommentId))
                    .willThrow(new NotFoundCommentException(CommentErrorCode.NOT_FOUND_COMMENT));

            //when & then
            assertThrows(NotFoundCommentException.class, () -> commentService.deleteComment(nonExistingCommentId, user.getId()));
        }

        @Test
        void Comment_삭제_시_본인의_comment가_아니면_에러를_반환() {
            //given
            given(userRepository.findUserByIdWithThrow(2L)).willReturn(user2);
            Comment comment = Comment.builder()
                    .user(user)
                    .review(review)
                    .content("content")
                    .build();
            ReflectionTestUtils.setField(comment, "id", 1L);
            given(commentRepository.findCommentByIdWithThrow(1L)).willReturn(comment);

            //when & then
            assertThrows(NotPermissionCommentAuthorityException.class, () -> commentService.deleteComment(comment.getId(), user2.getId()));

            verify(commentRepository, never()).delete(any());
        }
    }



}