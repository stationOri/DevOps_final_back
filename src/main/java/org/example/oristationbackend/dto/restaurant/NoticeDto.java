package org.example.oristationbackend.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeDto {
  private int restId;
  private String selectedDate;
  private String selectedTime;
  private String notice;

}
