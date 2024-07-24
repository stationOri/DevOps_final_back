package org.example.oristationbackend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NearRestDto {
  private int restId; // 식당 id
  private String restName; // 식당 이름
  private String restAddress; // 식당 주소
  private String restPhoto; // 식당 사진
  private String keyword1; // 키워드1
  private String keyword2; // 키워드2
  private String keyword3; // 키워드3
  private double lat; // 위도
  private double lng; // 경도
  private List<NearRestReviewDto> reviews; // 리뷰 리스트
}


