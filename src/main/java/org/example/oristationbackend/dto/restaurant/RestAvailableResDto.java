package org.example.oristationbackend.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.oristationbackend.entity.type.OpenDay;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RestAvailableResDto {
  private int restId; // 식당 id
  private OpenDay restDay; // 식당 영업 요일
  private Map<String, Map<String, Boolean>> availabilityMap = new TreeMap<>(); // 예약 가능 시간대 (키: 날짜, 값: 시간대 및 가능 여부)

  public void addAvailability(String date, Map<String, Boolean> available) {
    this.availabilityMap.computeIfAbsent(date, k -> new TreeMap<>()).putAll(available);
  }
}