package org.example.oristationbackend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRestaurant is a Querydsl query type for Restaurant
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRestaurant extends EntityPathBase<Restaurant> {

    private static final long serialVersionUID = -149977326L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRestaurant restaurant = new QRestaurant("restaurant");

    public final BooleanPath isBlocked = createBoolean("isBlocked");

    public final DatePath<java.sql.Date> joinDate = createDate("joinDate", java.sql.Date.class);

    public final QLogin login;

    public final DatePath<java.sql.Date> quitDate = createDate("quitDate", java.sql.Date.class);

    public final StringPath restAccount = createString("restAccount");

    public final QRestaurantInfo restaurantInfo;

    public final StringPath restData = createString("restData");

    public final NumberPath<Integer> restId = createNumber("restId", Integer.class);

    public final BooleanPath restIsopen = createBoolean("restIsopen");

    public final StringPath restName = createString("restName");

    public final StringPath restNum = createString("restNum");

    public final StringPath restOwner = createString("restOwner");

    public final StringPath restPhone = createString("restPhone");

    public final StringPath restPhoto = createString("restPhoto");

    public final EnumPath<org.example.oristationbackend.entity.type.RestaurantStatus> restStatus = createEnum("restStatus", org.example.oristationbackend.entity.type.RestaurantStatus.class);

    public QRestaurant(String variable) {
        this(Restaurant.class, forVariable(variable), INITS);
    }

    public QRestaurant(Path<? extends Restaurant> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRestaurant(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRestaurant(PathMetadata metadata, PathInits inits) {
        this(Restaurant.class, metadata, inits);
    }

    public QRestaurant(Class<? extends Restaurant> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.login = inits.isInitialized("login") ? new QLogin(forProperty("login"), inits.get("login")) : null;
        this.restaurantInfo = inits.isInitialized("restaurantInfo") ? new QRestaurantInfo(forProperty("restaurantInfo"), inits.get("restaurantInfo")) : null;
    }

}

