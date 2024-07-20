package org.example.oristationbackend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.oristationbackend.dto.restaurant.RestaurantOpenDto;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FavRestResDto {
  private int restId; // 식당 id
  private String restPhoto; // 식당 사진
  private String restName; // 식당 이름
  private String restAddress; // 식당 주소
  private List<RestaurantOpenDto> restOpentimes; // 식당 오픈 시간
  private String keyword1; // 키워드1
  private String keyword2; // 키워드2
  private String keyword3; // 키워드3
  private boolean isFavorite; // 즐겨찾기 여부
}
