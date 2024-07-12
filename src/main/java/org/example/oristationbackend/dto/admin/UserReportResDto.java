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
    int userReportId;
    String userEmail;
    String userName;
    Date reportDate;
    String reportContent;
    ReportStatus reportStatus;
    int adminId;

}
