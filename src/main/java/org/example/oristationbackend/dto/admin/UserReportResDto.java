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
    private String userEmail;
    private String userName;
    private Date reportDate;
    private String reportContent;
    private ReportStatus reportStatus;
    private int adminId;

}
