package org.example.oristationbackend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReview is a Querydsl query type for Review
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReview extends EntityPathBase<Review> {

    private static final long serialVersionUID = 1027433901L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReview review = new QReview("review");

    public final BooleanPath blind = createBoolean("blind");

    public final NumberPath<Integer> likeNum = createNumber("likeNum", Integer.class);

    public final QRestaurant restaurant;

    public final StringPath reviewData = createString("reviewData");

    public final DateTimePath<java.sql.Timestamp> reviewDate = createDateTime("reviewDate", java.sql.Timestamp.class);

    public final NumberPath<Double> reviewGrade = createNumber("reviewGrade", Double.class);

    public final NumberPath<Integer> reviewId = createNumber("reviewId", Integer.class);

    public final StringPath reviewImg = createString("reviewImg");

    public final StringPath reviewImg2 = createString("reviewImg2");

    public final StringPath reviewImg3 = createString("reviewImg3");

    public final ListPath<ReviewLikes, QReviewLikes> reviewLikes = this.<ReviewLikes, QReviewLikes>createList("reviewLikes", ReviewLikes.class, QReviewLikes.class, PathInits.DIRECT2);

    public final QUser user;

    public QReview(String variable) {
        this(Review.class, forVariable(variable), INITS);
    }

    public QReview(Path<? extends Review> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReview(PathMetadata metadata, PathInits inits) {
        this(Review.class, metadata, inits);
    }

    public QReview(Class<? extends Review> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.restaurant = inits.isInitialized("restaurant") ? new QRestaurant(forProperty("restaurant"), inits.get("restaurant")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

