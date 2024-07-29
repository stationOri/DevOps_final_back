package org.example.oristationbackend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRestaurantMenu is a Querydsl query type for RestaurantMenu
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRestaurantMenu extends EntityPathBase<RestaurantMenu> {

    private static final long serialVersionUID = 1192638353L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRestaurantMenu restaurantMenu = new QRestaurantMenu("restaurantMenu");

    public final NumberPath<Integer> menuId = createNumber("menuId", Integer.class);

    public final StringPath menuName = createString("menuName");

    public final StringPath menuPhoto = createString("menuPhoto");

    public final NumberPath<Integer> menuPrice = createNumber("menuPrice", Integer.class);

    public final QRestaurant restaurant;

    public QRestaurantMenu(String variable) {
        this(RestaurantMenu.class, forVariable(variable), INITS);
    }

    public QRestaurantMenu(Path<? extends RestaurantMenu> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRestaurantMenu(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRestaurantMenu(PathMetadata metadata, PathInits inits) {
        this(RestaurantMenu.class, metadata, inits);
    }

    public QRestaurantMenu(Class<? extends RestaurantMenu> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.restaurant = inits.isInitialized("restaurant") ? new QRestaurant(forProperty("restaurant"), inits.get("restaurant")) : null;
    }

}

