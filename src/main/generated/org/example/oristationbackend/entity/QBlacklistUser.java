package org.example.oristationbackend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBlacklistUser is a Querydsl query type for BlacklistUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBlacklistUser extends EntityPathBase<BlacklistUser> {

    private static final long serialVersionUID = -1151964525L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBlacklistUser blacklistUser = new QBlacklistUser("blacklistUser");

    public final QAdmin admin;

    public final DatePath<java.sql.Date> banStartDate = createDate("banStartDate", java.sql.Date.class);

    public final NumberPath<Integer> blacklistUserId = createNumber("blacklistUserId", Integer.class);

    public final EnumPath<org.example.oristationbackend.entity.type.BlackStatus> blackStatus = createEnum("blackStatus", org.example.oristationbackend.entity.type.BlackStatus.class);

    public final DateTimePath<java.sql.Timestamp> processingDate = createDateTime("processingDate", java.sql.Timestamp.class);

    public final NumberPath<Integer> reportNum = createNumber("reportNum", Integer.class);

    public final QUser user;

    public QBlacklistUser(String variable) {
        this(BlacklistUser.class, forVariable(variable), INITS);
    }

    public QBlacklistUser(Path<? extends BlacklistUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBlacklistUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBlacklistUser(PathMetadata metadata, PathInits inits) {
        this(BlacklistUser.class, metadata, inits);
    }

    public QBlacklistUser(Class<? extends BlacklistUser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.admin = inits.isInitialized("admin") ? new QAdmin(forProperty("admin"), inits.get("admin")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

