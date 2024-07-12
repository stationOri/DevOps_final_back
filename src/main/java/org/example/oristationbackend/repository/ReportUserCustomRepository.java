package org.example.oristationbackend.repository;

import org.example.oristationbackend.dto.admin.UserReportResDto;
import org.example.oristationbackend.entity.type.ReportStatus;

import java.util.List;

public interface ReportUserCustomRepository {
    List<UserReportResDto> findReportUserByUserId(int userId);
    List<UserReportResDto> findReportUserByStatus(ReportStatus status);
}
