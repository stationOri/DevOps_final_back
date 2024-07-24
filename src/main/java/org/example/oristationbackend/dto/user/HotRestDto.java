package org.example.oristationbackend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HotRestDto {
  private int restId; // 식당 id
  private String restName; // 식당 이름
  private String restPhoto; // 식당 사진
  private Double restGrade; // 식당 별점
  private String keyword1; // 키워드1
  private String keyword2; // 키워드2
  private String keyword3; // 키워드3
}
