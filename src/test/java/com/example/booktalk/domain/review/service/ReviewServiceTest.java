package com.example.booktalk.domain.review.service;

import com.example.booktalk.domain.comment.entity.Comment;
import com.example.booktalk.domain.comment.repository.CommentRepository;
import com.example.booktalk.domain.review.dto.request.ReviewCreateReq;
import com.example.booktalk.domain.review.dto.request.ReviewUpdateReq;
import com.example.booktalk.domain.review.dto.response.ReviewCreateRes;
import com.example.booktalk.domain.review.dto.response.ReviewDeleteRes;
import com.example.booktalk.domain.review.dto.response.ReviewGetRes;
import com.example.booktalk.domain.review.dto.response.ReviewUpdateRes;
import com.example.booktalk.domain.review.entity.Review;
import com.example.booktalk.domain.review.exception.NotFoundReviewException;
import com.example.booktalk.domain.review.exception.ReviewErrorCode;
import com.example.booktalk.domain.review.repository.ReviewRepository;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.repository.UserRepository;
import com.example.booktalk.global.exception.GlobalException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;


@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @InjectMocks
    ReviewService reviewService;

    @Mock
    ReviewRepository reviewRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    CommentRepository commentRepository;

    User user;
    User user2;

    @BeforeEach
    void init() {
        user = User.builder()
                .email("email@gmail.com")
                .password("12345678")
                .build();
        user2 = User.builder()
                .email("email2@gmail.com")
                .password("12345678")
                .build();
        ReflectionTestUtils.setField(user, "id", 1L);
        ReflectionTestUtils.setField(user2, "id", 2L);
    }
    @Nested
    class Review_생성_테스트 {

        @Test
        void Review_생성_성공() {
            //given
            given(userRepository.findUserByIdWithThrow(1L)).willReturn(user);
            ReviewCreateReq req = new ReviewCreateReq("title", "content");
            Review review = Review.builder()
                    .title(req.title())
                    .content(req.content())
                    .user(user)
                    .build();
            ReflectionTestUtils.setField(review, "id", 1L);
            given(reviewRepository.save(any())).willReturn(review);

            //when
            ReviewCreateRes result = reviewService.createReview(req, 1L);

            //then
            assertThat(result.reviewId()).isEqualTo(1L);
            assertThat(result.title()).isEqualTo("title");
            assertThat(result.content()).isEqualTo("content");
//            verify(reviewRepository, Mockito.times(1)).save(any(Review.class));
        }
    }

    @Nested
    class Review_조회_테스트 {

        @Test
        void Review_단건_조회_성공() {
            //given
            Review review = Review.builder()
                    .title("title")
                    .content("content")
                    .user(user)
                    .build();
            Comment comment1 = Comment.builder()
                    .user(user2)
                    .content("commentContent1")
                    .build();
            Comment comment2 = Comment.builder()
                    .user(user2)
                    .content("commentContent2")
                    .build();

            ReflectionTestUtils.setField(review, "id", 1L);
            ReflectionTestUtils.setField(comment1, "id", 1L);
            ReflectionTestUtils.setField(comment2, "id", 2L);
            List<Comment> commentList = new ArrayList<>();
            commentList.add(comment1);
            commentList.add(comment2);
            given(reviewRepository.findReviewByIdWithThrow(1L)).willReturn(review);
            given(commentRepository.findAllByReviewOrderByCreatedAtDesc(review)).willReturn(commentList);

            //when
            ReviewGetRes result = reviewService.getReview(1L);

            //then
            assertThat(result.reviewId()).isEqualTo(1L);
            assertThat(result.title()).isEqualTo("title");
            assertThat(result.content()).isEqualTo("content");

            assertThat(result.commentList().size()).isEqualTo(2);
            assertThat(result.commentList().get(0).commentId()).isEqualTo(1L);
            assertThat(result.commentList().get(0).content()).isEqualTo("commentContent1");
            assertThat(result.commentList().get(1).commentId()).isEqualTo(2L);
            assertThat(result.commentList().get(1).content()).isEqualTo("commentContent2");
        }

        @Test
        void Review_단건_조회_시_Review가_존재하지_않으면_에러를_반환한다() {
            //given
            given(reviewRepository.findReviewByIdWithThrow(1L))
                    .willThrow(new NotFoundReviewException(ReviewErrorCode.NOT_FOUND_REVIEW));

            //when & then
            assertThatThrownBy(() -> reviewService.getReview(1L))
                    .isInstanceOf(GlobalException.class)
                    .hasMessageContaining("해당하는 게시글이 존재하지 않습니다.");

            verify(reviewRepository).findReviewByIdWithThrow(1L);
            verifyNoInteractions(commentRepository);
        }

        @Test
        void Review_전체_조회_성공() {
            //given
            boolean isAsc = true;
            String sortBy = "id";

            Review review1 = Review.builder()
                    .title("title1")
                    .content("content1")
                    .user(user)
                    .build();
            Review review2 = Review.builder()
                    .title("title2")
                    .content("content2")
                    .user(user2)
                    .build();
            ReflectionTestUtils.setField(review1, "id", 1L);
            ReflectionTestUtils.setField(review2, "id", 2L);


        }


    }

    @Nested
    class Review_수정_테스트 {

        @Test
        void Reiew_수정_성공() {
            //given
            given(userRepository.findUserByIdWithThrow(1L)).willReturn(user);
            Review review = Review.builder()
                    .user(user)
                    .title("title")
                    .content("content")
                    .build();
            ReflectionTestUtils.setField(review, "id", 1L);
            ReviewUpdateReq req = new ReviewUpdateReq("update_title", "update_content");
            given(reviewRepository.findReviewByIdWithThrow(1L)).willReturn(review);

            //when
            ReviewUpdateRes result = reviewService.updateReview(1L, req, 1L);

            //then
            assertThat(result.id()).isEqualTo(1L);
            assertThat(result.title()).isEqualTo("update_title");
            assertThat(result.content()).isEqualTo("update_content");
        }
    }

    @Nested
    class Review_삭제_테스트 {

        @Test
        void Review_삭제_성공() {
            //given
            Review review = Review.builder()
                    .user(user)
                    .title("delete_title")
                    .content("delete_content")
                    .build();
            ReflectionTestUtils.setField(review, "id", 1L);
            given(reviewRepository.findReviewByIdWithThrow(1L)).willReturn(review);

            //when
            ReviewDeleteRes result = reviewService.deleteReview(1L, 1L);

            //then
            assertThat(result.msg()).isEqualTo("리뷰 게시글이 삭제되었습니다.");
        }
    }

}