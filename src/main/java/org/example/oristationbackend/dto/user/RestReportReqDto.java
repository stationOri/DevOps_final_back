package org.example.oristationbackend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestReportReqDto {
    private int userId;
    private int restId;
    private Date reportDate;
    private String reportContent;

}
