package org.example.oristationbackend.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.oristationbackend.entity.type.BlackStatus;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestBlackResDto {
  private int blacklistId; // 블랙리스트 id
  private String processingDate; // 블랙리스트 등록 날짜
  private String RestEmail; // 블랙리스트 등록 대상 식당 이메일
  private String AdminEmail; // 블랙리스트 등록한 관리자 이메일
  private String blackStatus; // 블랙리스트 상태
  private int reportNum; // 블랙리스트 신고 횟수
}
