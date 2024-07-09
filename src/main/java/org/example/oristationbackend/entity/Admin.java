package org.example.oristationbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "admin")
@AllArgsConstructor
@NoArgsConstructor
public class Admin {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int adminId;

  @Column(name = "admin_name", nullable = false, length = 20)
  private String adminName;

  @Column(name = "admin_phone", nullable = false, length = 11)
  private String adminPhone;

  @OneToOne
  @MapsId
  @JoinColumn(name = "admin_id")
  private Login login;
}