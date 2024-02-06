package com.example.booktalk.domain.product.repository;


import com.example.booktalk.domain.product.entity.Product;
import com.example.booktalk.domain.product.entity.QProduct;
import com.example.booktalk.domain.productcategory.entity.QProductCategory;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    QProduct product = QProduct.product;

    @Override
    public Page<Product> getPostListByName(Pageable pageable, String search) {

        JPAQuery<Product> query = jpaQueryFactory
            .selectFrom(product)
            .where(product.deleted.eq(false))
            .where(product.name.contains(search));
        //containsIgnoreCase // 대소문자 구별무시

        // 정렬 적용
        if (pageable.getSort().isSorted()) {
            for (Sort.Order order : pageable.getSort()) {
                PathBuilder<Product> pathBuilder = new PathBuilder<>(Product.class,
                    product.getMetadata());
                query.orderBy(new OrderSpecifier<>(order.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(order.getProperty(), Comparable.class)));
            }
        }

        List<Product> productList = query
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .distinct()
            .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
            .select(product.countDistinct())
            .from(product)
            .where(product.deleted.eq(false))
            .where(product.name.contains(search));

        long count = countQuery.fetchFirst() != null ? countQuery.fetchFirst() : 0L;

        return new PageImpl<>(productList, pageable, count);
    }

    @Override
    public Page<Product> getProductListByTag(Pageable pageable, String tag) {

        QProductCategory productCategory = QProductCategory.productCategory;

        JPAQuery<Product> query = jpaQueryFactory
            .selectFrom(product)
            .leftJoin(product.productCategoryList, productCategory).fetchJoin()
            .where(product.deleted.eq(false))
            .where(hasTag(tag));

        // 정렬 적용
        if (pageable.getSort().isSorted()) {
            for (Sort.Order order : pageable.getSort()) {
                PathBuilder<Product> pathBuilder = new PathBuilder<>(Product.class,
                    product.getMetadata());
                query.orderBy(new OrderSpecifier<>(order.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(order.getProperty(), Comparable.class)));
            }
        }

        long total = query.fetch().size();
        List<Product> productList = query
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .distinct()
            .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
            .select(product.countDistinct())
            .from(product)
            .leftJoin(product.productCategoryList, productCategory)
            .where(product.deleted.eq(false))
            .where(hasTag(tag));

        long count = countQuery.fetchFirst() != null ? countQuery.fetchFirst() : 0L;

        return new PageImpl<>(productList, pageable, count);
    }

    private BooleanExpression hasTag(String tagName) {
        return product.productCategoryList.any()
            .category.name.eq(tagName);
    }

}
