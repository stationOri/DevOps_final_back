package org.example.oristationbackend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRestTempHoliday is a Querydsl query type for RestTempHoliday
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRestTempHoliday extends EntityPathBase<RestTempHoliday> {

    private static final long serialVersionUID = 391743163L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRestTempHoliday restTempHoliday = new QRestTempHoliday("restTempHoliday");

    public final DatePath<java.sql.Date> endDate = createDate("endDate", java.sql.Date.class);

    public final QRestaurant restaurant;

    public final DatePath<java.sql.Date> startDate = createDate("startDate", java.sql.Date.class);

    public final NumberPath<Integer> tempHolidayId = createNumber("tempHolidayId", Integer.class);

    public QRestTempHoliday(String variable) {
        this(RestTempHoliday.class, forVariable(variable), INITS);
    }

    public QRestTempHoliday(Path<? extends RestTempHoliday> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRestTempHoliday(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRestTempHoliday(PathMetadata metadata, PathInits inits) {
        this(RestTempHoliday.class, metadata, inits);
    }

    public QRestTempHoliday(Class<? extends RestTempHoliday> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.restaurant = inits.isInitialized("restaurant") ? new QRestaurant(forProperty("restaurant"), inits.get("restaurant")) : null;
    }

}

