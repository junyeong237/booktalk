package com.example.booktalk.domain.product.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QProduct is a Querydsl query type for Product
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProduct extends EntityPathBase<Product> {

    private static final long serialVersionUID = 147582213L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QProduct product = new QProduct("product");

    public final com.example.booktalk.domain.common.QBaseEntity _super = new com.example.booktalk.domain.common.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final BooleanPath deleted = createBoolean("deleted");

    public final BooleanPath finished = createBoolean("finished");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedAt = _super.modifiedAt;

    public final StringPath name = createString("name");

    public final NumberPath<Long> price = createNumber("price", Long.class);

    public final ListPath<com.example.booktalk.domain.productcategory.entity.ProductCategory, com.example.booktalk.domain.productcategory.entity.QProductCategory> productCategoryList = this.<com.example.booktalk.domain.productcategory.entity.ProductCategory, com.example.booktalk.domain.productcategory.entity.QProductCategory>createList("productCategoryList", com.example.booktalk.domain.productcategory.entity.ProductCategory.class, com.example.booktalk.domain.productcategory.entity.QProductCategory.class, PathInits.DIRECT2);

    public final NumberPath<Long> productLikeCnt = createNumber("productLikeCnt", Long.class);

    public final NumberPath<Long> quantity = createNumber("quantity", Long.class);

    public final EnumPath<Region> region = createEnum("region", Region.class);

    public final com.example.booktalk.domain.user.entity.QUser user;

    public QProduct(String variable) {
        this(Product.class, forVariable(variable), INITS);
    }

    public QProduct(Path<? extends Product> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QProduct(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QProduct(PathMetadata metadata, PathInits inits) {
        this(Product.class, metadata, inits);
    }

    public QProduct(Class<? extends Product> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new com.example.booktalk.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

