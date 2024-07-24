package org.example.oristationbackend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecommendRestDto {
  private int restId; // 식당 id
  private String restName; // 식당 이름
  private String restPhoto; // 식당 사진
  private String restAddress; // 식당 주소
  private Double restGrade; // 식당 별점
}
