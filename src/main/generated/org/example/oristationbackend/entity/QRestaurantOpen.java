package org.example.oristationbackend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRestaurantOpen is a Querydsl query type for RestaurantOpen
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRestaurantOpen extends EntityPathBase<RestaurantOpen> {

    private static final long serialVersionUID = 1192708220L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRestaurantOpen restaurantOpen = new QRestaurantOpen("restaurantOpen");

    public final QRestaurant restaurant;

    public final NumberPath<Integer> restaurantOpenId = createNumber("restaurantOpenId", Integer.class);

    public final StringPath restBreakend = createString("restBreakend");

    public final StringPath restBreakstart = createString("restBreakstart");

    public final StringPath restClose = createString("restClose");

    public final EnumPath<org.example.oristationbackend.entity.type.OpenDay> restDay = createEnum("restDay", org.example.oristationbackend.entity.type.OpenDay.class);

    public final StringPath restLastorder = createString("restLastorder");

    public final StringPath restOpen = createString("restOpen");

    public QRestaurantOpen(String variable) {
        this(RestaurantOpen.class, forVariable(variable), INITS);
    }

    public QRestaurantOpen(Path<? extends RestaurantOpen> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRestaurantOpen(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRestaurantOpen(PathMetadata metadata, PathInits inits) {
        this(RestaurantOpen.class, metadata, inits);
    }

    public QRestaurantOpen(Class<? extends RestaurantOpen> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.restaurant = inits.isInitialized("restaurant") ? new QRestaurant(forProperty("restaurant"), inits.get("restaurant")) : null;
    }

}

