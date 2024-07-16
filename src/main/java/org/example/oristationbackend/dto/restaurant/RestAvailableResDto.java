package org.example.oristationbackend.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.oristationbackend.entity.type.OpenDay;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RestAvailableResDto {
  private int restId; // 식당 id
  private OpenDay restDay; // 식당 영업 요일
  private List<String> availableTimes; // 예약 가능 시간대
}
