package org.example.oristationbackend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.oristationbackend.entity.type.PaymentStatus;
import org.example.oristationbackend.entity.type.ReservationStatus;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserReservationResDto {
  private int resId; // 예약 id
  private int restId; // 식당 id
  private Timestamp resDatetime; // 예약 시간
  private String restName; // 식당 이름
  private String restPhoto; // 식당 사진
  private int resNum; // 예약 인원
  private ReservationStatus resStatus; // 예약 상태
  private PaymentStatus paymentStatus; // 결제 상태
  private int refund; // 환불될 예약금
}
