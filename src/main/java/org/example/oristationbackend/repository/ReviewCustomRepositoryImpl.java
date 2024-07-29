package org.example.oristationbackend.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
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
        QReview review = QReview.review;
        QReviewLikes likes = QReviewLikes.reviewLikes;

        BooleanExpression likeCondition = likes.user.userId.eq(userId).and(likes.review.reviewId.eq(review.reviewId));
        BooleanExpression reviewCondition = review.restaurant.restId.eq(restId);
        return queryFactory
                .select(Projections.fields(ReviewRestDto.class,
                        review.reviewId,
                        review.user.userNickname,review.reviewGrade,review.reviewData,review.reviewImg,
                        review.reviewImg2,review.reviewImg3,review.reviewDate,review.likeNum,review.blind,
                                likes.isNotNull().as("likedByUser")))
                .from(review)
                .leftJoin(likes).on(likeCondition)
                .where(reviewCondition)
                .fetch(); // 결과 리스트 반환;
    }


}
