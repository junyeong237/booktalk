package com.example.booktalk.domain.product.repository;


import com.example.booktalk.domain.category.entity.QCategory;
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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    QProduct product = QProduct.product;

    @Override
    public List<Product> getPostListByName(Sort sort, String search) {

        JPAQuery<Product> query = jpaQueryFactory
            .selectFrom(product)
            .where(product.deleted.eq(false))
            .where(hasProductName(search))
            .distinct();

        // 정렬 적용
        if (sort.isSorted()) {
            for (Sort.Order order : sort) {
                PathBuilder<Product> pathBuilder = new PathBuilder<>(Product.class,
                    product.getMetadata());
                query.orderBy(new OrderSpecifier<>(order.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(order.getProperty(), Comparable.class)));
            }
        }

        List<Product> productList = query
            .fetch();

        return productList;
    }

    @Override
    public List<Product> getProductListByTag(Sort sort, String tag) {

        QProductCategory productCategory = QProductCategory.productCategory;
        QCategory category = QCategory.category;

        JPAQuery<Product> query = jpaQueryFactory
            .selectFrom(product)
            .leftJoin(product.productCategoryList, productCategory).fetchJoin()
            .leftJoin(productCategory.category, category).fetchJoin()
            .where(product.deleted.eq(false))
            .where(hasTag(tag));

        // 정렬 적용
        if (sort.isSorted()) {
            for (Sort.Order order : sort) {
                PathBuilder<Product> pathBuilder = new PathBuilder<>(Product.class,
                    product.getMetadata());
                query.orderBy(new OrderSpecifier<>(order.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.get(order.getProperty(), Comparable.class)));
            }
        }

        List<Product> productList = query
            .fetch();

        return productList;
    }


    private BooleanExpression hasTag(String tagName) {
        return QCategory.category.name.eq(tagName);
    }

    private BooleanExpression hasProductName(String productName) {
        return product.name.eq(productName);
    }
}
