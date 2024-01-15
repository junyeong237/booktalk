package com.example.booktalk.domain.review.repository;

import com.example.booktalk.domain.review.entity.Review;
import com.example.booktalk.domain.review.exception.NotFoundReviewException;
import com.example.booktalk.domain.review.exception.ReviewErrorCode;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private NotFoundReviewException notFoundReviewException;

    User user1;
    User user2;

    @BeforeEach
    void init() {
        user1 = User.builder()
                .email("email@gmail.com")
                .password("12345678")
                .randomNickname("nickname1")
                .build();
        user2 = User.builder()
                .email("email2@gmail.com")
                .password("12345678")
                .randomNickname("nickname2")
                .build();
        userRepository.save(user1);
        userRepository.save(user2);
    }

    @Test
    @Transactional
    void findAll_테스트() {
        //given
        Review review1 = Review.builder()
                .title("title1")
                .content("content1")
                .user(user1)
                .build();
        Review review2 = Review.builder()
                .title("title2")
                .content("content2")
                .user(user2)
                .build();
        reviewRepository.save(review1);
        reviewRepository.save(review2);

        //when
        List<Review> result = reviewRepository.findAll();

        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getTitle()).isEqualTo("title1");
        assertThat(result.get(1).getTitle()).isEqualTo("title2");
    }

    @Nested
    class findReviewByIdWithThrow_테스트 {

        @Test
        @Transactional
        void findReviewByIdWithThrow_Review_반환_테스트() {
            //given
            Review review = Review.builder()
                    .title("title")
                    .content("content")
                    .user(user1)
                    .build();
            reviewRepository.save(review);

            //when
            Review result = reviewRepository.findReviewByIdWithThrow(review.getId());

            //then
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
        }

        @Test
        @Transactional
        void findReviewByIdWithThrow_exception_반환_테스트() {
            //given
            Long nonExistingReviewId = 100L;

            //when
            NotFoundReviewException exception = assertThrows(NotFoundReviewException.class,
                    () -> {reviewRepository.findReviewByIdWithThrow(nonExistingReviewId);
            });

            //then
            assertThat(exception.getErrorCode()).isEqualTo(ReviewErrorCode.NOT_FOUND_REVIEW);
        }

    }










}