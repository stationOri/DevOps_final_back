package org.example.oristationbackend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MostRestDto {
  private int restId; // 식당 id
  private String restName; // 식당 이름
  private String restAddress; // 식당 주소
  private String restPhone; // 식당 전화번호
  private Double restGrade; // 식당 별점
  private String restPhoto; // 식당 사진
  private int revCount; // 예약 수
}
