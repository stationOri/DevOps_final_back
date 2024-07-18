package org.example.oristationbackend.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.oristationbackend.dto.restaurant.WaitingRestResDto;
import org.example.oristationbackend.dto.user.ReviewRestDto;
import org.example.oristationbackend.dto.user.WaitingResDto;
import org.example.oristationbackend.entity.QRestaurant;
import org.example.oristationbackend.entity.QUser;
import org.example.oristationbackend.entity.QWaiting;
import org.example.oristationbackend.entity.type.RestWatingStatus;
import org.example.oristationbackend.entity.type.UserWaitingStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public class WaitingCustomRepositoryImpl implements WaitingCustomRepository{

    //@PersistenceContext
    //private EntityManager entityManager;
    private final EntityManager entityManager;
    private final JPAQueryFactory queryFactory;

    @Autowired
    public WaitingCustomRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.queryFactory = new JPAQueryFactory(entityManager);
    }
    @Override
    public Optional<WaitingResDto> findByUserId(int userId) {
        QWaiting waiting = QWaiting.waiting;
        QRestaurant restaurant = QRestaurant.restaurant;


        // 사용자가 등록한 웨이팅 정보의 최소 대기일시를 조회
        Timestamp minWaitingDate = queryFactory
                .select(waiting.waitingDate.max())
                .from(waiting)
                .where(waiting.user.userId.eq(userId)
                        .and(waiting.waitingDate.goe(startoftoday())))
                .fetchOne();


        List<WaitingResDto> resultList = queryFactory
                .select(Projections.fields(WaitingResDto.class,
                        waiting.waitingId,
                        waiting.waitingNum,
                        Expressions.as(

                                JPAExpressions.select(waiting.count().intValue())
                                        .from(waiting)
                                        .join(waiting.restaurant)
                                        .where(waiting.restaurant.restId.eq(restaurant.restId)
                                                .and(waiting.waitingDate.goe(startoftoday()))
                                                .and(waiting.waitingDate.lt(minWaitingDate))
                                                .and(waiting.userWaitingStatus.eq(UserWaitingStatus.IN_QUEUE))),

                                "waitingLeft"

                        ),
                        waiting.waitingPpl,
                        waiting.userWaitingStatus.as("waitingStatus"),
                        restaurant.restName

                ))
                .from(waiting)
                .join(waiting.restaurant, restaurant)
                .where(waiting.user.userId.eq(userId)
                        .and(waiting.waitingDate.goe(startoftoday())))
                .fetch();

        return resultList.isEmpty() ? Optional.empty() : Optional.of(resultList.get(0));
    }

    @Override
    public List<WaitingRestResDto> findByRestId(int restId) {
        QWaiting waiting = QWaiting.waiting;
        QUser user = QUser.user;


        List<WaitingRestResDto> resultList = queryFactory
                .select(Projections.fields(WaitingRestResDto.class,
                        waiting.waitingId,
                        waiting.waitingNum,
                        waiting.waitingPpl,
                        user.userName,
                        waiting.userWaitingStatus.as("waitingStatus")
                ))
                .from(waiting)
                .join(user).on(waiting.user.userId.eq(user.userId))
                .where(
                        waiting.restaurant.restId.eq(restId)
                                .and(waiting.waitingDate.goe(startoftoday()))
                )
                .fetch();

        return resultList;
    }
    @Override
    public long countTodayByRestId(int restId) {
        QWaiting waiting = QWaiting.waiting;
        QUser user = QUser.user;

        long count = queryFactory
                .select(waiting.count())
                .from(waiting)
                .join(user).on(waiting.user.userId.eq(user.userId))
                .where(
                        waiting.restaurant.restId.eq(restId)
                                .and(waiting.waitingDate.goe(startoftoday()))
                )
                .fetchOne();

        return count;
    }


    @Override
    public boolean existByUserIdAndDate(int userId) {
        QWaiting waiting = QWaiting.waiting;
        QUser user = QUser.user;

        boolean exists = queryFactory
                .select(waiting.waitingId)
                .from(waiting)
                .where(
                        waiting.user.userId.eq(userId)
                                .and(waiting.waitingDate.goe(startoftoday()))
                                .and(waiting.userWaitingStatus.eq(UserWaitingStatus.IN_QUEUE))
                )
                .fetchFirst() != null;

        return exists;
    }
    private Timestamp startoftoday() {
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = LocalDateTime.of(today, LocalTime.MIN);
        return java.sql.Timestamp.valueOf(startOfDay);
    }
}

