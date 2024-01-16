package com.example.booktalk.domain.productLike.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProductLike is a Querydsl query type for ProductLike
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProductLike extends EntityPathBase<ProductLike> {

    private static final long serialVersionUID = 1067974565L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProductLike productLike = new QProductLike("productLike");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isProductLiked = createBoolean("isProductLiked");

    public final com.example.booktalk.domain.product.entity.QProduct product;

    public final com.example.booktalk.domain.user.entity.QUser user;

    public QProductLike(String variable) {
        this(ProductLike.class, forVariable(variable), INITS);
    }

    public QProductLike(Path<? extends ProductLike> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProductLike(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProductLike(PathMetadata metadata, PathInits inits) {
        this(ProductLike.class, metadata, inits);
    }

    public QProductLike(Class<? extends ProductLike> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.product = inits.isInitialized("product") ? new com.example.booktalk.domain.product.entity.QProduct(forProperty("product"), inits.get("product")) : null;
        this.user = inits.isInitialized("user") ? new com.example.booktalk.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

