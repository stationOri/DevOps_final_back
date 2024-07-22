package org.example.oristationbackend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserInfoResDto {
  private int userId; // 사용자 id
  private String userName; // 사용자 이름
  private String userNickname; // 사용자 닉네임
  private String userPhone; // 사용자 전화번호
  private String userEmail; // 사용자 이메일
  private String joinDate; //가입일
  private String quitDate;
  private boolean isBlocked; // 사용자 정지 여부
  private boolean isBanned; // 사용자 탈퇴 여부
}
