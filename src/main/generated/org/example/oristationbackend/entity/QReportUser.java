package org.example.oristationbackend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReportUser is a Querydsl query type for ReportUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReportUser extends EntityPathBase<ReportUser> {

    private static final long serialVersionUID = -741067020L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReportUser reportUser = new QReportUser("reportUser");

    public final QAdmin admin;

    public final StringPath reportContent = createString("reportContent");

    public final DatePath<java.sql.Date> reportDate = createDate("reportDate", java.sql.Date.class);

    public final EnumPath<org.example.oristationbackend.entity.type.ReportStatus> reportStatus = createEnum("reportStatus", org.example.oristationbackend.entity.type.ReportStatus.class);

    public final QRestaurant restaurant;

    public final QReview review;

    public final QUser user;

    public final NumberPath<Integer> userReportId = createNumber("userReportId", Integer.class);

    public QReportUser(String variable) {
        this(ReportUser.class, forVariable(variable), INITS);
    }

    public QReportUser(Path<? extends ReportUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReportUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReportUser(PathMetadata metadata, PathInits inits) {
        this(ReportUser.class, metadata, inits);
    }

    public QReportUser(Class<? extends ReportUser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.admin = inits.isInitialized("admin") ? new QAdmin(forProperty("admin"), inits.get("admin")) : null;
        this.restaurant = inits.isInitialized("restaurant") ? new QRestaurant(forProperty("restaurant"), inits.get("restaurant")) : null;
        this.review = inits.isInitialized("review") ? new QReview(forProperty("review"), inits.get("review")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

