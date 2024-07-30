package org.example.oristationbackend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewLikes is a Querydsl query type for ReviewLikes
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewLikes extends EntityPathBase<ReviewLikes> {

    private static final long serialVersionUID = 99643919L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewLikes reviewLikes = new QReviewLikes("reviewLikes");

    public final NumberPath<Integer> likeId = createNumber("likeId", Integer.class);

    public final QReview review;

    public final QUser user;

    public QReviewLikes(String variable) {
        this(ReviewLikes.class, forVariable(variable), INITS);
    }

    public QReviewLikes(Path<? extends ReviewLikes> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewLikes(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewLikes(PathMetadata metadata, PathInits inits) {
        this(ReviewLikes.class, metadata, inits);
    }

    public QReviewLikes(Class<? extends ReviewLikes> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.review = inits.isInitialized("review") ? new QReview(forProperty("review"), inits.get("review")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

