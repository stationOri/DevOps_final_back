package org.example.oristationbackend.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.oristationbackend.entity.type.ReportStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserReportStatusReqDto {
    private int userReportId;
    private ReportStatus reportStatus;
    private int adminId;
}
