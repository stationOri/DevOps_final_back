package org.example.oristationbackend.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.core.types.dsl.Expressions;
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

        WaitingResDto result = queryFactory
                .select(Projections.fields(WaitingResDto.class,
                        waiting.waitingId,
                        waiting.waitingNum,
                        // 서브쿼리: 같은 레스토랑에서 해당 사용자보다 앞에 있는 웨이팅 레코드의 수를 계산
                        queryFactory.select(waiting.count())
                                .from(waiting)
                                .where(waiting.restaurant.restId.eq(
                                        queryFactory.select(waiting.restaurant.restId)
                                                        .from(waiting)
                                                        .where(waiting.user.userId.eq(userId)
                                                                .and(waiting.waitingDate.goe(startoftoday())))
                                        ).and(waiting.waitingNum.lt(
                                        queryFactory.select(waiting.waitingNum)
                                                        .from(waiting)
                                                        .where(waiting.user.userId.eq(userId)
                                                                .and(waiting.waitingDate.goe(startoftoday())))
                                        ))
                                ),
                        waiting.waitingPpl,
                        restaurant.restName,
                        waiting.userWaitingStatus
                ))
                .from(waiting)
                .join(waiting.restaurant, restaurant)
                .where(waiting.user.userId.eq(userId))
                .fetchOne();

        return Optional.ofNullable(result);
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
                        waiting.userWaitingStatus
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

        // 오늘 날짜의 시작 (00:00:00)

        long count = Optional.ofNullable(queryFactory
                .select(waiting.count())
                .from(waiting)
                .join(user).on(waiting.user.userId.eq(user.userId))
                .where(
                        waiting.restaurant.restId.eq(restId)
                                .and(waiting.waitingDate.goe(startoftoday()))

                )
                .fetchOne()
        ).orElse(0L);

        return count;
    }

    @Override
    public boolean existByUserIdAndDate(int userId) {
        QWaiting waiting = QWaiting.waiting;
        QUser user = QUser.user;
        // 쿼리 실행 및 존재 여부 확인
        boolean exists = queryFactory
                .select(waiting.waitingId)
                .from(waiting)
                .where(
                        waiting.user.userId.eq(userId)
                                .and(waiting.waitingDate.goe(startoftoday())
                                        .and(waiting.userWaitingStatus.eq(UserWaitingStatus.IN_QUEUE)))
                )
                .fetchFirst() != null; // 결과가 존재하면 true, 없으면 false 반환

        return exists;
    }

    private Timestamp startoftoday(){
        LocalDate today = LocalDate.now();
        LocalDateTime startOfDay = LocalDateTime.of(today, LocalTime.MIN);
        return Timestamp.valueOf(startOfDay);

    }
}

