package org.example.oristationbackend.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.oristationbackend.entity.type.ReportStatus;

import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserReportResDto {
    private int userReportId;
    private String userName;
    private String reportDate;
    private String reviewContent;
    private String reportContent;
    private String reporter_id;
    private String reportStatus;
    private String adminId;

}
