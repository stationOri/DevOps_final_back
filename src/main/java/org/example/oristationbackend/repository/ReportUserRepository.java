package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.ReportUser;
import org.example.oristationbackend.entity.type.ReportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;

public interface ReportUserRepository extends JpaRepository<ReportUser, Integer>,ReportUserCustomRepository {
    int countByUserUserIdAndReportStatus(int userId, ReportStatus reportStatus);
}
