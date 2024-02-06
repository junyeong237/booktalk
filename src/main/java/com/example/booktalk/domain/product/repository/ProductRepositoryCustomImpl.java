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
import java.util.stream.Collectors;
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

        List<Long> productIds = getProductIds(query);

// 가져온 id들을 기반으로 다시 해당 product들을 조회합니다.
        List<Product> productList = getProductsPageByIds(productIds, pageable);

        long count = productIds.size();

        return new PageImpl<>(productList, pageable, count);
    }

    private BooleanExpression hasTag(String tagName) {
        return product.productCategoryList.any()
            .category.name.eq(tagName);
    }

    // 처음에 쿼리를 실행하여 결과로 나온 Product의 id들을 가져오는 메서드
    private List<Long> getProductIds(JPAQuery<Product> query) {
        // 쿼리 실행 후 결과로 나온 Product 엔티티의 id를 리스트로 변환하여 반환
        return query
            .fetch()
            .stream()
            .map(Product::getId)
            .collect(Collectors.toList());
    }

    // getProductIds() 메서드를 사용하여 다시 product를 조회하는 메서드
    private List<Product> getProductsPageByIds(List<Long> productIds, Pageable pageable) {
        // productIds에 해당하는 Product 엔티티들을 조회하여 리스트로 반환
        return jpaQueryFactory
            .selectFrom(product)
            .where(product.id.in(productIds))
            .distinct()
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    }
}
