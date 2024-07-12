package org.example.oristationbackend.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.oristationbackend.dto.user.ChatMessageDto;
import org.example.oristationbackend.dto.user.ChatRoomDto;
import org.example.oristationbackend.entity.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@Transactional
public class CustomChatRoomRepositoryImpl implements CustomChatRoomRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ChatRoomDto> findChatRoomsByUserId(int userId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QChatRoom chatRoom = QChatRoom.chatRoom;
        QLogin questioner = QLogin.login;
        QLogin answerer = QLogin.login;
        QUser questionerUser = QUser.user;
        QAdmin questionerAdmin = QAdmin.admin;
        QRestaurant questionerRestaurant = QRestaurant.restaurant;
        QUser answererUser = QUser.user;
        QAdmin answererAdmin = QAdmin.admin;
        QRestaurant answererRestaurant = QRestaurant.restaurant;

        // 동적 조건을 위한 BooleanBuilder
        BooleanBuilder whereClause = new BooleanBuilder();

        // userId와 questioner의 id가 일치하는 경우
        whereClause.or(questioner.user.userId.eq(userId));
        whereClause.or(questioner.restaurant.restId.eq(userId));
        whereClause.or(questioner.admin.adminId.eq(userId));
        // userId와 answerer의 id가 일치하는 경우
        whereClause.or(answerer.user.userId.eq(userId));
        whereClause.or(answerer.restaurant.restId.eq(userId));
        whereClause.or(answerer.admin.adminId.eq(userId));

        QMessage subMessage = new QMessage("subMessage");
        var subQuery = JPAExpressions.select(subMessage.messageContent)
                .from(subMessage)
                .where(subMessage.chatRoom.chattingRoomId.eq(chatRoom.chattingRoomId))
                .orderBy(subMessage.sendTime.desc())
                .limit(1);
        return queryFactory
                .select(Projections.fields(ChatRoomDto.class,
                        chatRoom.chattingRoomId.as("chattingRoomId"),
                        new CaseBuilder()
                                .when(questioner.user.userId.isNotNull()).then(questionerUser.userName)
                                .when(questioner.restaurant.restId.isNotNull()).then(questionerRestaurant.restName)
                                .when(questioner.admin.adminId.isNotNull()).then(questionerAdmin.adminName)
                                .otherwise("unknown")
                                .as("qsName"),
                        new CaseBuilder()
                                .when(answerer.user.userId.isNotNull()).then(answererUser.userName)
                                .when(answerer.restaurant.restId.isNotNull()).then(answererRestaurant.restName)
                                .when(answerer.admin.adminId.isNotNull()).then(answererAdmin.adminName)
                                .otherwise("unknown")
                                .as("ansName"),
                        Expressions.asString(subQuery).as("lastMsg")))
                .from(chatRoom)
                .leftJoin(chatRoom.questioner, questioner)
                .leftJoin(questioner.user, questionerUser)
                .leftJoin(questioner.restaurant, questionerRestaurant)
                .leftJoin(questioner.admin, questionerAdmin)
                .leftJoin(chatRoom.answerer, answerer)
                .leftJoin(answerer.user, answererUser)
                .leftJoin(answerer.admin, questionerAdmin)
                .leftJoin(answerer.restaurant, answererRestaurant)
                .where(whereClause) // 동적 조건 적용
                .fetch();
    }
    @Override
    public List<ChatMessageDto> findMessagesByChatRoomId(int chatRoomId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);

        QMessage message = QMessage.message;
        QUser user = QUser.user;
        QRestaurant restaurant = QRestaurant.restaurant;
        QChatRoom qChatRoom = QChatRoom.chatRoom;
        QAdmin admin = QAdmin.admin;

        // ChatRoom에서 answererId와 questionerId 조회
        Tuple result = queryFactory.select(qChatRoom.answerer.loginId, qChatRoom.questioner.loginId)
                .from(qChatRoom)
                .where(qChatRoom.chattingRoomId.eq(chatRoomId))
                .fetchOne();

        if (result == null) {
            throw new IllegalArgumentException("Invalid chatRoomId: " + chatRoomId);
        }

        Integer answererId = result.get(qChatRoom.answerer.loginId);
        Integer questionerId = result.get(qChatRoom.questioner.loginId);

        // QueryDSL을 이용하여 쿼리 작성 및 실행
        return queryFactory
                .select(Projections.fields(ChatMessageDto.class,
                        // senderType 설정
                        new CaseBuilder()
                                .when(message.senderId.eq(questionerId)).then("qs")
                                .when(message.senderId.eq(answererId)).then("ans")
                                .otherwise("unknown")
                                .as("senderType"),
                        // senderName 설정
                        new CaseBuilder()
                                .when(message.senderId.eq(user.userId)).then(user.userName)
                                .when(message.senderId.eq(restaurant.restId)).then(restaurant.restName)
                                .when(message.senderId.eq(admin.adminId)).then(admin.adminName)
                                .otherwise("unknown")
                                .as("senderName"),
                        message.messageContent, // messageContent 필드 매핑
                        message.sendTime)) // sendTime 필드 매핑
                .from(message) // message 엔티티를 기준으로 조회
                .leftJoin(user).on(message.senderId.eq(user.userId)) // senderId가 userId와 일치하는 경우 조인
                .leftJoin(restaurant).on(message.senderId.eq(restaurant.restId)) // senderId가 restaurantId와 일치하는 경우 조인
                .leftJoin(admin).on(message.senderId.eq(admin.adminId)) // senderId가 adminId와 일치하는 경우 조인
                .where(message.chatRoom.chattingRoomId.eq(chatRoomId)) // chatRoomId와 일치하는 메시지 조회
                .fetch(); // 결과 리스트 반환
    }

}

