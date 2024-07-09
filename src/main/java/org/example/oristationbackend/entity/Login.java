package org.example.oristationbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.oristationbackend.entity.type.ChatType;

@Entity
@Getter
@Table(name = "login")
@AllArgsConstructor
@NoArgsConstructor
public class Login {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int loginId;

  @Column(name = "email", nullable = false, length = 30)
  private String email;

  @Column(name = "password", nullable = false, length = 15)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", length = 10, insertable = false, updatable = false)
  private ChatType chatType;

//  @OneToOne(mappedBy = "login", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//  @JoinColumn(name="user_id")
//  private User user;

  @OneToOne(mappedBy = "login", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Admin admin;

//  @OneToOne(mappedBy = "login", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//  private Restaurant restaurant;
}