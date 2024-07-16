package org.example.oristationbackend.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MenuListResDto {
  private int menuId; // 메뉴 id
  private int restId; // 식당 id
  private String menuName; // 메뉴 이름
  private int menuPrice; // 메뉴 가격
  private String menuPhoto; // 메뉴 사진
}
