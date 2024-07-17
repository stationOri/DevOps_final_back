package org.example.oristationbackend.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MenuModReqDto {
  private int menuPrice; // 메뉴 가격
  private String menuPhoto; // 메뉴 사진
}
