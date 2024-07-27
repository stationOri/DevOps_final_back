package org.example.oristationbackend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWaiting is a Querydsl query type for Waiting
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWaiting extends EntityPathBase<Waiting> {

    private static final long serialVersionUID = 1802039960L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWaiting waiting = new QWaiting("waiting");

    public final QRestaurant restaurant;

    public final QUser user;

    public final EnumPath<org.example.oristationbackend.entity.type.UserWaitingStatus> userWaitingStatus = createEnum("userWaitingStatus", org.example.oristationbackend.entity.type.UserWaitingStatus.class);

    public final DateTimePath<java.sql.Timestamp> waitingDate = createDateTime("waitingDate", java.sql.Timestamp.class);

    public final NumberPath<Integer> waitingId = createNumber("waitingId", Integer.class);

    public final StringPath waitingName = createString("waitingName");

    public final NumberPath<Integer> waitingNum = createNumber("waitingNum", Integer.class);

    public final StringPath waitingPhone = createString("waitingPhone");

    public final NumberPath<Integer> waitingPpl = createNumber("waitingPpl", Integer.class);

    public QWaiting(String variable) {
        this(Waiting.class, forVariable(variable), INITS);
    }

    public QWaiting(Path<? extends Waiting> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWaiting(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWaiting(PathMetadata metadata, PathInits inits) {
        this(Waiting.class, metadata, inits);
    }

    public QWaiting(Class<? extends Waiting> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.restaurant = inits.isInitialized("restaurant") ? new QRestaurant(forProperty("restaurant"), inits.get("restaurant")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

