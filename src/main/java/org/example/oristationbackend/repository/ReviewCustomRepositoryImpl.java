package org.example.oristationbackend.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.oristationbackend.dto.user.ReviewRestDto;
import org.example.oristationbackend.entity.QReview;
import org.example.oristationbackend.entity.QReviewLikes;
import org.example.oristationbackend.entity.QUser;
import org.example.oristationbackend.entity.Review;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class ReviewCustomRepositoryImpl implements ReviewCustomRepository{
    @PersistenceContext
    private EntityManager entityManager;
    QReview review=QReview.review;
    QUser user=QUser.user;
    QReviewLikes likes=QReviewLikes.reviewLikes;
    @Override
    public List<ReviewRestDto> findReviewAndLikesByRestaurantIdAndUserId(int restId, int userId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        return queryFactory
                .select(Projections.fields(ReviewRestDto.class,
                        review.user.userNickname,review.reviewGrade,review.reviewData,review.reviewImg,
                        review.reviewImg2,review.reviewImg3,review.reviewDate,review.likeNum,review.blind,
                                likes.isNotNull().as("likedByUser")))
                .from(review)
                .leftJoin(likes).on(review.user.userId.eq(likes.user.userId).and(likes.user.userId.eq(userId))) // senderId가 restaurantId와 일치하는 경우 조인
                .where(review.restaurant.restId.eq(restId)) // chatRoomId와 일치하는 메시지 조회
                .fetch(); // 결과 리스트 반환;
    }
}
