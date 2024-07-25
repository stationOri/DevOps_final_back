package org.example.oristationbackend.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.oristationbackend.entity.type.ReportStatus;

import java.sql.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RestReportListResDto {
    private int restReportId;
    private String restName; // 식당 이름
    private String reportDate;
    private String reportContent;
    private String reporterId;
    private String reportStatus;
    private String adminId;
    private int RestId;
}
