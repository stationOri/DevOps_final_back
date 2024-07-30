package org.example.oristationbackend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReservation is a Querydsl query type for Reservation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReservation extends EntityPathBase<Reservation> {

    private static final long serialVersionUID = 1489470103L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReservation reservation = new QReservation("reservation");

    public final QPayment payment;

    public final NumberPath<Integer> refund = createNumber("refund", Integer.class);

    public final DateTimePath<java.sql.Timestamp> reqDatetime = createDateTime("reqDatetime", java.sql.Timestamp.class);

    public final StringPath request = createString("request");

    public final DateTimePath<java.sql.Timestamp> resDatetime = createDateTime("resDatetime", java.sql.Timestamp.class);

    public final ListPath<ReservedMenu, QReservedMenu> reservedMenus = this.<ReservedMenu, QReservedMenu>createList("reservedMenus", ReservedMenu.class, QReservedMenu.class, PathInits.DIRECT2);

    public final NumberPath<Integer> resId = createNumber("resId", Integer.class);

    public final NumberPath<Integer> resNum = createNumber("resNum", Integer.class);

    public final QRestaurant restaurant;

    public final EnumPath<org.example.oristationbackend.entity.type.ReservationStatus> status = createEnum("status", org.example.oristationbackend.entity.type.ReservationStatus.class);

    public final DateTimePath<java.sql.Timestamp> statusChangedDate = createDateTime("statusChangedDate", java.sql.Timestamp.class);

    public final QUser user;

    public QReservation(String variable) {
        this(Reservation.class, forVariable(variable), INITS);
    }

    public QReservation(Path<? extends Reservation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReservation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReservation(PathMetadata metadata, PathInits inits) {
        this(Reservation.class, metadata, inits);
    }

    public QReservation(Class<? extends Reservation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.payment = inits.isInitialized("payment") ? new QPayment(forProperty("payment"), inits.get("payment")) : null;
        this.restaurant = inits.isInitialized("restaurant") ? new QRestaurant(forProperty("restaurant"), inits.get("restaurant")) : null;
        this.user = inits.isInitialized("user") ? new QUser(forProperty("user"), inits.get("user")) : null;
    }

}

