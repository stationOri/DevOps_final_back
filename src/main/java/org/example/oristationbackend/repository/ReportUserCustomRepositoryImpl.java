package org.example.oristationbackend.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.oristationbackend.dto.admin.UserReportResDto;
import org.example.oristationbackend.dto.user.ChatRoomDto;
import org.example.oristationbackend.entity.*;
import org.example.oristationbackend.entity.type.ReportStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class ReportUserCustomRepositoryImpl implements ReportUserCustomRepository {
    @PersistenceContext
    private EntityManager entityManager;

    //아래 두 방법중에 어떤걸로 할지 고민하기!!!!!!
    @Override
    public List<UserReportResDto> findReportUserByUserId(int userId) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QReportUser reportUser= QReportUser.reportUser;
        QUser user= QUser.user;
        QLogin login = QLogin.login;

        return queryFactory
                .select(Projections.constructor(UserReportResDto.class,
                        reportUser.userReportId,
                        login.email,
                        user.userName,
                        reportUser.reportDate,
                        reportUser.reportContent,
                        reportUser.reportStatus,
                        reportUser.admin.adminId))
                .from(reportUser)
                .join(reportUser.user, user)
                .join(user.login, login) // Join with Login entity to get email
                .where(user.userId.eq(userId))
                .fetch();

    }

    @Override
    public List<UserReportResDto> findReportUserByStatus(ReportStatus status) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QReportUser reportUser = QReportUser.reportUser;

        return queryFactory
                .select(Projections.constructor(UserReportResDto.class,
                        reportUser.userReportId,
                        reportUser.user.login.email,
                        reportUser.user.userName,
                        reportUser.reportDate,
                        reportUser.reportContent,
                        reportUser.reportStatus,
                        reportUser.admin.adminId))
                .from(reportUser)
                .where(reportUser.reportStatus.eq(status))
                .fetch();
    }
}

