package org.example.oristationbackend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBlacklistRest is a Querydsl query type for BlacklistRest
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBlacklistRest extends EntityPathBase<BlacklistRest> {

    private static final long serialVersionUID = -1152066916L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBlacklistRest blacklistRest = new QBlacklistRest("blacklistRest");

    public final QAdmin admin;

    public final DatePath<java.sql.Date> banStartDate = createDate("banStartDate", java.sql.Date.class);

    public final NumberPath<Integer> blacklistRestId = createNumber("blacklistRestId", Integer.class);

    public final EnumPath<org.example.oristationbackend.entity.type.BlackStatus> blackStatus = createEnum("blackStatus", org.example.oristationbackend.entity.type.BlackStatus.class);

    public final DateTimePath<java.sql.Timestamp> processingDate = createDateTime("processingDate", java.sql.Timestamp.class);

    public final NumberPath<Integer> reportNum = createNumber("reportNum", Integer.class);

    public final QRestaurant restaurant;

    public QBlacklistRest(String variable) {
        this(BlacklistRest.class, forVariable(variable), INITS);
    }

    public QBlacklistRest(Path<? extends BlacklistRest> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBlacklistRest(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBlacklistRest(PathMetadata metadata, PathInits inits) {
        this(BlacklistRest.class, metadata, inits);
    }

    public QBlacklistRest(Class<? extends BlacklistRest> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.admin = inits.isInitialized("admin") ? new QAdmin(forProperty("admin"), inits.get("admin")) : null;
        this.restaurant = inits.isInitialized("restaurant") ? new QRestaurant(forProperty("restaurant"), inits.get("restaurant")) : null;
    }

}

