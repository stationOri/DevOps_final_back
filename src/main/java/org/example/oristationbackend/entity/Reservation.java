package org.example.oristationbackend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Reservation {
  @Id
  private int reservationId;
}
