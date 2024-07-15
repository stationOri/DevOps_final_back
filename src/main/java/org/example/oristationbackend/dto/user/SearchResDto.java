package org.example.oristationbackend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.oristationbackend.entity.Restaurant;
import org.example.oristationbackend.entity.RestaurantInfo;
import org.example.oristationbackend.entity.type.ReservationType;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SearchResDto {
  private int restId; // 식당 id
  private String restName; // 식당 이름
  private String restAddress; // 식당 주소
  private String restPhone; // 식당 전화번호
  private Double restGrade; // 식당 별점
  private String restPhoto; // 식당 사진
  private String restIntro; // 식당 소개
  private String restPost; // 가게 공지
  private ReservationType revWait; // 예약 대기 방식
  private String keyword1; // 키워드1
  private String keyword2; // 키워드2
  private String keyword3; // 키워드3

  public SearchResDto(Restaurant restaurant, RestaurantInfo restaurantInfo) {
    this.restId = restaurant.getRestId();
    this.restName = restaurant.getRestName();
    this.restAddress = restaurantInfo.getRestAddress();
    this.restPhone = restaurant.getRestPhone();
    this.restGrade = restaurantInfo.getRestGrade();
    this.restPhoto = restaurant.getRestPhoto();
    this.restIntro = restaurantInfo.getRestIntro();
    this.restPost = restaurantInfo.getRestPost();
    this.revWait = restaurantInfo.getRevWait();
    if (restaurantInfo.getKeyword1() != null) {
      this.keyword1 = restaurantInfo.getKeyword1().getKeyword();
    }
    if (restaurantInfo.getKeyword2() != null) {
      this.keyword2 = restaurantInfo.getKeyword2().getKeyword();
    }
    if (restaurantInfo.getKeyword3() != null) {
      this.keyword3 = restaurantInfo.getKeyword3().getKeyword();
    }
  }
}
