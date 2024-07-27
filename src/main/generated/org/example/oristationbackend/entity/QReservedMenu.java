package org.example.oristationbackend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReservedMenu is a Querydsl query type for ReservedMenu
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReservedMenu extends EntityPathBase<ReservedMenu> {

    private static final long serialVersionUID = -972170436L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReservedMenu reservedMenu = new QReservedMenu("reservedMenu");

    public final NumberPath<Integer> amount = createNumber("amount", Integer.class);

    public final QReservation reservation;

    public final QRestaurantMenu restaurantMenu;

    public final NumberPath<Integer> rev_menu_id = createNumber("rev_menu_id", Integer.class);

    public QReservedMenu(String variable) {
        this(ReservedMenu.class, forVariable(variable), INITS);
    }

    public QReservedMenu(Path<? extends ReservedMenu> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReservedMenu(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReservedMenu(PathMetadata metadata, PathInits inits) {
        this(ReservedMenu.class, metadata, inits);
    }

    public QReservedMenu(Class<? extends ReservedMenu> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.reservation = inits.isInitialized("reservation") ? new QReservation(forProperty("reservation"), inits.get("reservation")) : null;
        this.restaurantMenu = inits.isInitialized("restaurantMenu") ? new QRestaurantMenu(forProperty("restaurantMenu"), inits.get("restaurantMenu")) : null;
    }

}

