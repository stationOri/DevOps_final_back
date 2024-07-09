package org.example.oristationbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.oristationbackend.entity.type.ReservationStatus;

import java.sql.Timestamp;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int res_id;

  private Timestamp req_datetime;
  private Timestamp res_datetime;
  private Timestamp status_changed_date;
  private int res_num;
  private ReservationStatus status;
  private int refund;

  @Column(length=200)
  private String request;

//  @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="user_id")
//    private User user;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="rest_id")
//    private Restaurant restaurant;
}
