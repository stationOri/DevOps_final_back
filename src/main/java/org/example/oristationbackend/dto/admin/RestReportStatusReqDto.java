package org.example.oristationbackend.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.oristationbackend.entity.type.ReportStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestReportStatusReqDto {
    private int restReportId;
    private ReportStatus reportStatus;
    private int adminId;
}
