package org.example.oristationbackend.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRestaurantInfo is a Querydsl query type for RestaurantInfo
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRestaurantInfo extends EntityPathBase<RestaurantInfo> {

    private static final long serialVersionUID = 1192527584L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRestaurantInfo restaurantInfo = new QRestaurantInfo("restaurantInfo");

    public final QKeyword keyword1;

    public final QKeyword keyword2;

    public final QKeyword keyword3;

    public final NumberPath<Double> lat = createNumber("lat", Double.class);

    public final NumberPath<Double> lng = createNumber("lng", Double.class);

    public final NumberPath<Integer> maxPpl = createNumber("maxPpl", Integer.class);

    public final StringPath restAddress = createString("restAddress");

    public final QRestaurant restaurant;

    public final NumberPath<Integer> restDeposit = createNumber("restDeposit", Integer.class);

    public final EnumPath<org.example.oristationbackend.entity.type.MoneyMethod> restDepositMethod = createEnum("restDepositMethod", org.example.oristationbackend.entity.type.MoneyMethod.class);

    public final NumberPath<Double> restGrade = createNumber("restGrade", Double.class);

    public final NumberPath<Integer> restId = createNumber("restId", Integer.class);

    public final StringPath restIntro = createString("restIntro");

    public final StringPath restPhone = createString("restPhone");

    public final StringPath restPost = createString("restPost");

    public final EnumPath<org.example.oristationbackend.entity.type.MinuteType> restReserveInterval = createEnum("restReserveInterval", org.example.oristationbackend.entity.type.MinuteType.class);

    public final EnumPath<org.example.oristationbackend.entity.type.PeriodType> restReserveopenRule = createEnum("restReserveopenRule", org.example.oristationbackend.entity.type.PeriodType.class);

    public final NumberPath<Integer> restTablenum = createNumber("restTablenum", Integer.class);

    public final EnumPath<org.example.oristationbackend.entity.type.RestWatingStatus> restWaitingStatus = createEnum("restWaitingStatus", org.example.oristationbackend.entity.type.RestWatingStatus.class);

    public final EnumPath<org.example.oristationbackend.entity.type.ReservationType> revWait = createEnum("revWait", org.example.oristationbackend.entity.type.ReservationType.class);

    public QRestaurantInfo(String variable) {
        this(RestaurantInfo.class, forVariable(variable), INITS);
    }

    public QRestaurantInfo(Path<? extends RestaurantInfo> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRestaurantInfo(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRestaurantInfo(PathMetadata metadata, PathInits inits) {
        this(RestaurantInfo.class, metadata, inits);
    }

    public QRestaurantInfo(Class<? extends RestaurantInfo> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.keyword1 = inits.isInitialized("keyword1") ? new QKeyword(forProperty("keyword1")) : null;
        this.keyword2 = inits.isInitialized("keyword2") ? new QKeyword(forProperty("keyword2")) : null;
        this.keyword3 = inits.isInitialized("keyword3") ? new QKeyword(forProperty("keyword3")) : null;
        this.restaurant = inits.isInitialized("restaurant") ? new QRestaurant(forProperty("restaurant"), inits.get("restaurant")) : null;
    }

}

