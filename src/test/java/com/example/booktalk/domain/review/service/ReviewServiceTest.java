package com.example.booktalk.domain.review.service;

import com.example.booktalk.domain.comment.entity.Comment;
import com.example.booktalk.domain.comment.repository.CommentRepository;
import com.example.booktalk.domain.imageFile.service.ImageFileService;
import com.example.booktalk.domain.product.entity.Product;
import com.example.booktalk.domain.product.entity.Region;
import com.example.booktalk.domain.product.repository.ProductRepository;
import com.example.booktalk.domain.review.dto.request.ReviewCreateReq;
import com.example.booktalk.domain.review.dto.request.ReviewUpdateReq;
import com.example.booktalk.domain.review.dto.response.*;
import com.example.booktalk.domain.review.entity.Review;
import com.example.booktalk.domain.review.exception.NotFoundReviewException;
import com.example.booktalk.domain.review.exception.NotPermissionReviewAuthorityException;
import com.example.booktalk.domain.review.exception.ReviewErrorCode;
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
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


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

    @Mock
    ProductRepository productRepository;

    @Mock
    ImageFileService imageFileService;

    User user;
    User user2;
    Product product;

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
        product = Product.builder()
                .name("name")
                .price(1000L)
                .quantity(1L)
                .region(Region.BUSAN)
                .content("content")
                .user(user)
                .build();
        ReflectionTestUtils.setField(user, "id", 1L);
        ReflectionTestUtils.setField(user2, "id", 2L);
        ReflectionTestUtils.setField(product, "id", 1L);
    }
    @Nested
    class Review_생성_테스트 {

        @Test
        void Review_생성_성공() throws IOException {
            //given
            Long productId = 1L;
            given(productRepository.findProductByIdWithThrow(1L)).willReturn(product);
            given(userRepository.findUserByIdWithThrow(1L)).willReturn(user);
            ReviewCreateReq req = new ReviewCreateReq("title", "content", productId);
            MockMultipartFile file = new MockMultipartFile("file", "image.jpg", "image/jpeg", "image_content".getBytes());

            Review review = Review.builder()
                    .title(req.title())
                    .content(req.content())
                    .product(product)
                    .reviewImagePathUrl("image_url")
                    .user(user)
                    .build();
            ReflectionTestUtils.setField(review, "id", 1L);
            given(reviewRepository.save(any())).willReturn(review);

            //when
            ReviewCreateRes result = reviewService.createReview(req, 1L, file);

            //then
            assertThat(result.reviewId()).isEqualTo(1L);
            assertThat(result.title()).isEqualTo("title");
            assertThat(result.content()).isEqualTo("content");
            assertThat(result.nickname()).isEqualTo(user.getNickname());
            assertThat(result.getReviewImagePathUrl()).isEqualTo("image_url");

            verify(reviewRepository, times(1)).save(any(Review.class));
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
                    .product(product)
                    .reviewImagePathUrl("image_url")
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
        void Review_단건_조회_시_Review가_존재하지_않으면_에러를_반환() {
            //given
            Long nonExistingReviewId = 100L;

            given(reviewRepository.findReviewByIdWithThrow(nonExistingReviewId))
                    .willThrow(new NotFoundReviewException(ReviewErrorCode.NOT_FOUND_REVIEW));

            //when & then
            assertThrows(NotFoundReviewException.class, () -> reviewService.getReview(nonExistingReviewId));
        }

        @Test
        void Review_전체_조회_성공() {
            //given
            Review review1 = Review.builder()
                    .title("title1")
                    .content("content1")
                    .product(product)
                    .reviewImagePathUrl("image1_url")
                    .user(user)
                    .build();
            Review review2 = Review.builder()
                    .title("title2")
                    .content("content2")
                    .product(product)
                    .reviewImagePathUrl("image2_url")
                    .user(user2)
                    .build();
            ReflectionTestUtils.setField(review1, "id", 1L);
            ReflectionTestUtils.setField(review2, "id", 2L);

            List<Review> reviewList = new ArrayList<>();
            reviewList.add(review1);
            reviewList.add(review2);

            Sort.Direction direction = Sort.Direction.ASC;
            String sortBy = "id";
            Sort sort = Sort.by(direction, sortBy);

            given(reviewRepository.findAll(sort)).willReturn(reviewList);

            //when
            List<ReviewGetListRes> result = reviewService.getReviewList(sortBy, true);

            //then
            assertThat(result.size()).isEqualTo(2);

            assertThat(result.get(0).reviewId()).isEqualTo(1L);
            assertThat(result.get(0).title()).isEqualTo("title1");
            assertThat(result.get(0).name()).isEqualTo("nickname1");

            assertThat(result.get(1).reviewId()).isEqualTo(2L);
            assertThat(result.get(1).title()).isEqualTo("title2");
            assertThat(result.get(1).name()).isEqualTo("nickname2");

            verify(reviewRepository, times(1)).findAll(sort);
        }
    }

    @Test
    void Review_검색_성공() {
        //given
        Review review1 = Review.builder()
                .title("title1")
                .content("content")
                .product(product)
                .reviewImagePathUrl("image1_url")
                .user(user)
                .build();
        Review review2 = Review.builder()
                .title("title2")
                .content("content")
                .product(product)
                .reviewImagePathUrl("image2_url")
                .user(user2)
                .build();
        ReflectionTestUtils.setField(review1, "id", 1L);
        ReflectionTestUtils.setField(review2, "id", 2L);

        List<Review> reviewList = new ArrayList<>();
        reviewList.add(review1);
        reviewList.add(review2);

        Sort.Direction direction = Sort.Direction.ASC;
        String sortBy = "id";
        Sort sort = Sort.by(direction, sortBy);
        String search = "content";

        given(reviewRepository.getReviewListByTitleOrContent(sort, search))
                .willReturn(reviewList);

        //when
        List<ReviewSearchListRes> result =
                reviewService.getReviewSearchList(sortBy, true, search);

        //then
        assertThat(result.size()).isEqualTo(2);

        assertThat(result.get(0).title()).isEqualTo("title1");
        assertThat(result.get(1).title()).isEqualTo("title2");


        verify(reviewRepository, times(1))
                .getReviewListByTitleOrContent(sort, search);
    }

    @Nested
    class Review_수정_테스트 {

        @Test
        void Reiew_수정_성공() throws IOException {
            //given
            given(userRepository.findUserByIdWithThrow(1L)).willReturn(user);
            Review review = Review.builder()
                    .user(user)
                    .title("title")
                    .content("content")
                    .product(product)
                    .reviewImagePathUrl("image_url")
                    .build();
            ReflectionTestUtils.setField(review, "id", 1L);
            ReviewUpdateReq req = new ReviewUpdateReq("update_title", "update_content");
            MockMultipartFile file = new MockMultipartFile("file", "image.jpg", "image/jpeg", "image_content".getBytes());
            String updatedImagePathUrl = "update_image_url";
            given(reviewRepository.findReviewByIdWithThrow(1L)).willReturn(review);
            given(imageFileService.imageUpload(file)).willReturn(updatedImagePathUrl);

            //when
            ReviewUpdateRes result = reviewService.updateReview(1L, req, 1L, file);

            //then
            assertThat(result.id()).isEqualTo(1L);
            assertThat(result.title()).isEqualTo("update_title");
            assertThat(result.content()).isEqualTo("update_content");
            assertThat(result.reviewImagePathUrl()).isEqualTo("update_image_url");
        }

        @Test
        void Review_수정_시_Review가_존재하지_않으면_에러를_반환() {
            //given
            given(userRepository.findUserByIdWithThrow(1L)).willReturn(user);
            Long nonExistingReviewId = 100L;
            ReviewUpdateReq req = new ReviewUpdateReq("update_title", "update_content");
            MockMultipartFile file = new MockMultipartFile("file", "image.jpg", "image/jpeg", "image_content".getBytes());

            given(reviewRepository.findReviewByIdWithThrow(nonExistingReviewId))
                    .willThrow(new NotFoundReviewException(ReviewErrorCode.NOT_FOUND_REVIEW));

            //when & then
            assertThrows(NotFoundReviewException.class, () -> reviewService.updateReview(nonExistingReviewId, req, 1L, file));
        }

        @Test
        void Review_수정_시_본인의_review가_아니면_에러를_반환() {
            //given
            given(userRepository.findUserByIdWithThrow(2L)).willReturn(user2);
            Review review = Review.builder()
                    .user(user)
                    .title("title")
                    .content("content")
                    .build();
            ReflectionTestUtils.setField(review, "id", 1L);
            ReviewUpdateReq req = new ReviewUpdateReq("update_title", "update_content");
            given(reviewRepository.findReviewByIdWithThrow(1L)).willReturn(review);
            MockMultipartFile file = new MockMultipartFile("file", "image.jpg", "image/jpeg", "image_content".getBytes());

            //when & then
            assertThrows(NotPermissionReviewAuthorityException.class, () -> reviewService.updateReview(1L, req, 2L, file));

            verify(reviewRepository, never()).save(any());
        }
    }

    @Nested
    class Review_삭제_테스트 {

        @Test
        void Review_삭제_성공() {
            //given
            given(userRepository.findUserByIdWithThrow(1L)).willReturn(user);
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

        @Test
        void Review_삭제_시_Review가_존재하지_않으면_에러를_반환() {
            //given
            given(userRepository.findUserByIdWithThrow(1L)).willReturn(user);
            Long nonExistingReviewId = 100L;

            given(reviewRepository.findReviewByIdWithThrow(nonExistingReviewId))
                    .willThrow(new NotFoundReviewException(ReviewErrorCode.NOT_FOUND_REVIEW));

            //when & then
            assertThrows(NotFoundReviewException.class, () -> reviewService.deleteReview(nonExistingReviewId, 1L));
        }

        @Test
        void Review_삭제_시_본인의_review가_아니면_에러를_반환() {
            //given
            given(userRepository.findUserByIdWithThrow(2L)).willReturn(user2);
            Review review = Review.builder()
                    .user(user)
                    .title("delete_title")
                    .content("delete_content")
                    .build();
            ReflectionTestUtils.setField(review, "id", 1L);
            given(reviewRepository.findReviewByIdWithThrow(1L)).willReturn(review);

            //when & then
            assertThrows(NotPermissionReviewAuthorityException.class, () -> reviewService.deleteReview(1L, 2L));

            verify(reviewRepository, never()).delete(any());
        }
    }

}