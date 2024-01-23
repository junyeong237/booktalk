package com.example.booktalk.domain.reviewlike.service;

import com.example.booktalk.domain.review.entity.Review;
import com.example.booktalk.domain.review.repository.ReviewRepository;
import com.example.booktalk.domain.reviewlike.dto.response.ReviewLikeToggleRes;
import com.example.booktalk.domain.reviewlike.entity.ReviewLike;
import com.example.booktalk.domain.reviewlike.exception.NotPermissionToggleException;
import com.example.booktalk.domain.reviewlike.repository.ReviewLikeRepository;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewLikeServiceTest {

    @InjectMocks
    ReviewLikeService reviewLikeService;

    @Mock
    ReviewLikeRepository reviewLikeRepository;

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
                .build();
        user2 = User.builder()
                .email("email2@gmail.com")
                .password("12345678")
                .randomNickname("nickname2")
                .build();
        ReflectionTestUtils.setField(user, "id", 1L);
        ReflectionTestUtils.setField(user2, "id", 2L);
        review = Mockito.mock(Review.class);
        when(review.getId()).thenReturn(1L);
        when(review.getUser()).thenReturn(user);
    }

    @Test
    void 게시글_좋아요_성공() {
        //given
        given(userRepository.findUserByIdWithThrow(2L)).willReturn(user2);
        given(reviewRepository.findReviewByIdWithThrow(1L)).willReturn(review);
        given(reviewLikeRepository.findByReviewAndUser(review, user2)).willReturn(Optional.empty());
        ReviewLike reviewLike = ReviewLike.builder()
                .user(user2)
                .review(review)
                .build();
        ReflectionTestUtils.setField(reviewLike, "id", 1L);

        //when
        ReviewLikeToggleRes result = reviewLikeService.toggleReviewLike(review.getId(), user2.getId());

        //then
        verify(reviewLikeRepository).save(any(ReviewLike.class));
        verify(review).increaseReviewLike();
        assertThat(result.msg()).isEqualTo("좋아요!");
    }

    @Test
    void 게시글_좋아요_시_본인의_review면_에러를_반환() {
        //given
        given(userRepository.findUserByIdWithThrow(1L)).willReturn(user);
        given(reviewRepository.findReviewByIdWithThrow(1L)).willReturn(review);

        //when & then
        assertThrows(NotPermissionToggleException.class, () -> reviewLikeService.toggleReviewLike(review.getId(), user.getId()));

        verify(reviewLikeRepository, never()).save(any());
    }
}