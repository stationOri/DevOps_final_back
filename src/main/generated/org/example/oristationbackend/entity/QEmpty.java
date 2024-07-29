package org.example.oristationbackend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QEmpty is a Querydsl query type for Empty
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QEmpty extends EntityPathBase<Empty> {

    private static final long serialVersionUID = 852654168L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QEmpty empty = new QEmpty("empty1");

    public final DatePath<java.sql.Date> date = createDate("date", java.sql.Date.class);

    public final NumberPath<Integer> emptyId = createNumber("emptyId", Integer.class);

    public final NumberPath<Integer> people = createNumber("people", Integer.class);

    public final QRestaurant restaurant;

    public final BooleanPath status = createBoolean("status");

    public final TimePath<java.sql.Time> time = createTime("time", java.sql.Time.class);

    public final QUser user;

    public QEmpty(String variable) {
        this(Empty.class, forVariable(variable), INITS);
    }

    public QEmpty(Path<? extends Empty> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QEmpty(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QEmpty(PathMetadata metadata, PathInits inits) {
        this(Empty.class, metadata, inits);
    }

    public QEmpty(Class<? extends Empty> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.restaurant = inits.isInitialized("restaurant") ? new QRestaurant(forProperty("restaurant"), inits.get("restaurant")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

