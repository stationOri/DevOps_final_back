package org.example.oristationbackend.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.SubQueryExpression;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.Subquery;
import org.example.oristationbackend.dto.user.ChatMessageDto;
import org.example.oristationbackend.dto.user.ChatRoomDto;
import org.example.oristationbackend.entity.*;
import org.example.oristationbackend.entity.type.UserWaitingStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
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
        QLogin questionerLogin = new QLogin("questionerLogin");
        QLogin answererLogin = new QLogin("answererLogin");
        QUser questionerUser = new QUser("questionerUser");
        QAdmin questionerAdmin = new QAdmin("questionerAdmin");
        QRestaurant questionerRestaurant = new QRestaurant("questionerRestaurant");
        QUser answererUser = new QUser("answererUser");
        QAdmin answererAdmin = new QAdmin("answererAdmin");
        QRestaurant answererRestaurant = new QRestaurant("answererRestaurant");

        // 동적 조건을 위한 BooleanBuilder
        BooleanBuilder whereClause = new BooleanBuilder();

        // userId와 questioner의 id가 일치하는 경우
        whereClause.or(questionerLogin.user.userId.eq(userId));
        whereClause.or(questionerLogin.restaurant.restId.eq(userId));
        whereClause.or(questionerLogin.admin.adminId.eq(userId));
        // userId와 answerer의 id가 일치하는 경우
        whereClause.or(answererLogin.user.userId.eq(userId));
        whereClause.or(answererLogin.restaurant.restId.eq(userId));
        whereClause.or(answererLogin.admin.adminId.eq(userId));

        QMessage subMessage = new QMessage("subMessage");

        List<ChatRoomDto> chatRoomDtos=queryFactory
                .select(Projections.fields(ChatRoomDto.class,
                        chatRoom.chattingRoomId.as("chattingRoomId"),
                        new CaseBuilder()
                                .when(questionerLogin.user.userId.isNotNull()).then(questionerUser.userName)
                                .when(questionerLogin.restaurant.restId.isNotNull()).then(questionerRestaurant.restName)
                                .when(questionerLogin.admin.adminId.isNotNull()).then(questionerAdmin.adminName)
                                .otherwise("unknown")
                                .as("qsName"),
                        new CaseBuilder()
                                .when(answererLogin.user.userId.isNotNull()).then(answererUser.userName)
                                .when(answererLogin.restaurant.restId.isNotNull()).then(answererRestaurant.restName)
                                .when(answererLogin.admin.adminId.isNotNull()).then(answererAdmin.adminName)
                                .otherwise("unknown")
                                .as("ansName"),
                        questionerLogin.loginId.as("qsId"),   // 질문자 ID
                        answererLogin.loginId.as("ansId")))
                .from(chatRoom)

                .leftJoin(chatRoom.questioner, questionerLogin)
                .leftJoin(questionerLogin.user, questionerUser)
                .leftJoin(questionerLogin.restaurant, questionerRestaurant)
                .leftJoin(questionerLogin.admin, questionerAdmin)
                .leftJoin(chatRoom.answerer, answererLogin)
                .leftJoin(answererLogin.user, answererUser)
                .leftJoin(answererLogin.restaurant, answererRestaurant)
                .leftJoin(answererLogin.admin, answererAdmin)
                .where(whereClause) // 동적 조건 적용
                .fetch();

// Step 3: Update DTOs with the last message
        Map<Integer, String> lastMsgMap = chatRoomDtos.stream()
                .map(ChatRoomDto::getChattingRoomId)
                .collect(Collectors.toMap(
                        roomId -> roomId,
                        roomId -> {
                            String messageContent = queryFactory
                                    .select(subMessage.messageContent)
                                    .from(subMessage)
                                    .where(subMessage.chatRoom.chattingRoomId.eq(roomId))
                                    .orderBy(subMessage.sendTime.desc())
                                    .limit(1)
                                    .fetchOne();
                            return messageContent != null ? messageContent : "No Messages"; // Default value for null
                        }
                ));
        return chatRoomDtos.stream()
                .map(dto -> new ChatRoomDto(
                        dto.getChattingRoomId(),
                        dto.getQsName(),
                        dto.getQsId(),
                        dto.getAnsName(),
                        dto.getAnsId(),
                        lastMsgMap.get(dto.getChattingRoomId()) // Retrieve last message, handle null
                ))
                .collect(Collectors.toList());
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

