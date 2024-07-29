package org.example.oristationbackend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReportRest is a Querydsl query type for ReportRest
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReportRest extends EntityPathBase<ReportRest> {

    private static final long serialVersionUID = -741169411L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReportRest reportRest = new QReportRest("reportRest");

    public final QAdmin admin;

    public final StringPath reportContent = createString("reportContent");

    public final DatePath<java.sql.Date> reportDate = createDate("reportDate", java.sql.Date.class);

    public final EnumPath<org.example.oristationbackend.entity.type.ReportStatus> reportStatus = createEnum("reportStatus", org.example.oristationbackend.entity.type.ReportStatus.class);

    public final QRestaurant restaurant;

    public final NumberPath<Integer> restReportId = createNumber("restReportId", Integer.class);

    public final QUser user;

    public QReportRest(String variable) {
        this(ReportRest.class, forVariable(variable), INITS);
    }

    public QReportRest(Path<? extends ReportRest> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReportRest(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReportRest(PathMetadata metadata, PathInits inits) {
        this(ReportRest.class, metadata, inits);
    }

    public QReportRest(Class<? extends ReportRest> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.admin = inits.isInitialized("admin") ? new QAdmin(forProperty("admin"), inits.get("admin")) : null;
        this.restaurant = inits.isInitialized("restaurant") ? new QRestaurant(forProperty("restaurant"), inits.get("restaurant")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

