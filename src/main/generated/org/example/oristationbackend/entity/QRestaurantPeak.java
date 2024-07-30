package org.example.oristationbackend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRestaurantPeak is a Querydsl query type for RestaurantPeak
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRestaurantPeak extends EntityPathBase<RestaurantPeak> {

    private static final long serialVersionUID = 1192727313L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRestaurantPeak restaurantPeak = new QRestaurantPeak("restaurantPeak");

    public final DatePath<java.sql.Date> dateEnd = createDate("dateEnd", java.sql.Date.class);

    public final DatePath<java.sql.Date> dateStart = createDate("dateStart", java.sql.Date.class);

    public final NumberPath<Integer> peakId = createNumber("peakId", Integer.class);

    public final DateTimePath<java.sql.Timestamp> peakOpendate = createDateTime("peakOpendate", java.sql.Timestamp.class);

    public final QRestaurant restaurant;

    public QRestaurantPeak(String variable) {
        this(RestaurantPeak.class, forVariable(variable), INITS);
    }

    public QRestaurantPeak(Path<? extends RestaurantPeak> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRestaurantPeak(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRestaurantPeak(PathMetadata metadata, PathInits inits) {
        this(RestaurantPeak.class, metadata, inits);
    }

    public QRestaurantPeak(Class<? extends RestaurantPeak> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.restaurant = inits.isInitialized("restaurant") ? new QRestaurant(forProperty("restaurant"), inits.get("restaurant")) : null;
    }

}

