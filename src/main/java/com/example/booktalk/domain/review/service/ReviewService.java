package com.example.booktalk.domain.review.service;

import com.example.booktalk.domain.comment.dto.response.CommentGetListRes;
import com.example.booktalk.domain.comment.repository.CommentRepository;
import com.example.booktalk.domain.imageFile.service.ImageFileService;
import com.example.booktalk.domain.product.entity.Product;
import com.example.booktalk.domain.product.repository.ProductRepository;
import com.example.booktalk.domain.review.dto.request.ReviewCreateReq;
import com.example.booktalk.domain.review.dto.request.ReviewUpdateReq;
import com.example.booktalk.domain.review.dto.response.*;
import com.example.booktalk.domain.review.entity.Review;
import com.example.booktalk.domain.review.exception.NotPermissionReviewAuthorityException;
import com.example.booktalk.domain.review.exception.ReviewErrorCode;
import com.example.booktalk.domain.review.repository.ReviewRepository;
import com.example.booktalk.domain.user.entity.User;
import com.example.booktalk.domain.user.entity.UserRoleType;
import com.example.booktalk.domain.user.repository.UserRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final ImageFileService imageFileService;
    private final ProductRepository productRepository;

    public ReviewCreateRes createReview(ReviewCreateReq req, Long userId, MultipartFile file)
        throws IOException {
        User user = userRepository.findUserByIdWithThrow(userId);
        String reviewImagePathUrl;
        if (file!=null) {
            reviewImagePathUrl = imageFileService.imageUpload(file);
        } else {
            reviewImagePathUrl = null;
        }
        Product product = productRepository.findProductByIdWithThrow(req.productId());
        Review review = Review.builder()
            .title(req.title())
            .content(req.content())
            .product(product)
            .reviewImagePathUrl(reviewImagePathUrl)
            .user(user)
            .build();

        Review result = reviewRepository.save(review);

        return new ReviewCreateRes(product.getUser().getId(),result.getId(), product.getId(), result.getTitle(),
            result.getContent(), result.getUser().getNickname(), result.getReviewImagePathUrl());
    }

    @Transactional(readOnly = true)
    public List<ByMeReviewGetListRes> byMeGetReviewList(String sortBy, boolean isAsc, Long userId) {

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        List<Product> productList = productRepository.findProductsByUserId(userId);


        List<ByMeReviewGetListRes> result = new ArrayList<>();

        for (Product product : productList) {
            // 각 상품에 대한 리뷰 가져오기
            List<Review> reviewList = reviewRepository.findByProductId(product.getId(), sort);

            result.addAll(reviewList.stream()
                    .map(review -> new ByMeReviewGetListRes(
                            review.getId(),
                            review.getTitle(),
                            review.getUser().getNickname(),
                            review.getReviewLikeCount()
                    ))
                    .toList());
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<ReviewGetListRes> getReviewList(String sortBy, boolean isAsc) {

        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        List<Review> reviewList = reviewRepository.findAll(sort);

        return reviewList.stream()
                .map(review -> new ReviewGetListRes(review.getId(),
                        review.getTitle(), review.getUser().getNickname(),
                        review.getReviewLikeCount()))
                .toList();
    }


    @Transactional(readOnly = true)
    public List<ReviewSearchListRes> getReviewSearchList(String sortBy, boolean isAsc,
        String search) {
        Sort.Direction direction = isAsc ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, sortBy);

        List<Review> reviewList = reviewRepository.getReviewListByTitleOrContent(sort, search);

        return reviewList.stream()
            .map(review -> new ReviewSearchListRes(review.getId(),
                review.getTitle(), review.getUser().getNickname(),
                review.getReviewLikeCount()))
            .toList();
    }

    @Transactional(readOnly = true)
    public ReviewGetRes getReview(Long reviewId) {

        Review review = reviewRepository.findReviewByIdWithThrow(reviewId);

        List<CommentGetListRes> commentList = commentRepository.findAllByReviewOrderByCreatedAtDesc(
                review)
            .stream().map(comment -> new CommentGetListRes(comment.getId(),
                comment.getContent(), comment.getUser().getNickname()))
            .toList();

        return new ReviewGetRes(review.getId(), review.getProduct().getId(), review.getTitle(),
            review.getContent(), review.getUser().getNickname(),
            review.getReviewLikeCount(), review.getCreatedAt(), review.getModifiedAt(),
            review.getReviewImagePathUrl(), commentList);
    }

    public ReviewUpdateRes updateReview(Long reviewId, ReviewUpdateReq req, Long userId,
        MultipartFile file) throws IOException {

        User user = userRepository.findUserByIdWithThrow(userId);
        Review review = reviewRepository.findReviewByIdWithThrow(reviewId);
        validateReviewUser(user, review);

        if (file!=null) {
            String reviewImagePathUrl = imageFileService.imageUpload(file);
            review.update(req, reviewImagePathUrl);
        } else {
            review.update(req, review.getReviewImagePathUrl());
        }

        return new ReviewUpdateRes(review.getId(), review.getProduct().getId(), review.getTitle(),
            review.getContent(), review.getReviewImagePathUrl());
    }

    public ReviewDeleteRes deleteReview(Long reviewId, Long userId) {

        User user = userRepository.findUserByIdWithThrow(userId);
        Review review = reviewRepository.findReviewByIdWithThrow(reviewId);
        validateReviewUser(user, review);

        reviewRepository.delete(review);

        return new ReviewDeleteRes("리뷰 게시글이 삭제되었습니다.");

    }

    public void validateReviewUser(User user, Review review) {
        if (!user.getId().equals(review.getUser().getId())
            && !user.getRole().equals(UserRoleType.ADMIN)) {
            throw new NotPermissionReviewAuthorityException(
                ReviewErrorCode.NOT_PERMISSION_REVIEW_AUTHORITY);
        }
    }


}
