package com.example.booktalk.domain.review.repository;

import com.example.booktalk.domain.review.entity.QReview;
import com.example.booktalk.domain.review.entity.Review;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryCustomImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    QReview review = QReview.review;

    @Override
    public List<Review> getReviewListByTitleOrContent(Sort sort, String search) {

        JPAQuery<Review> query = jpaQueryFactory
                .selectFrom(review)
                .where(hasReviewTitleOrContent(search))
                .distinct();

        if(sort.isSorted()) {
            for(Sort.Order order : sort) {
                PathBuilder<Review> pathBuilder = new PathBuilder<>(Review.class, review.getMetadata());
                query.orderBy(new OrderSpecifier<>(order.isAscending() ? Order.ASC : Order.DESC,
                        pathBuilder.get(order.getProperty(), Comparable.class)));
            }
        }

        List<Review> reviewList = query.fetch();

        return reviewList;
    }

    private BooleanExpression hasReviewTitleOrContent(String search) {
        return review.title.containsIgnoreCase(search)
                .or(review.content.containsIgnoreCase(search));
    }

}
