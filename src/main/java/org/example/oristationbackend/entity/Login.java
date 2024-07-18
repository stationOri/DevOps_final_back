package org.example.oristationbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.oristationbackend.entity.type.ChatType;

@Entity
@Data
@Table(name = "login")
@AllArgsConstructor
@NoArgsConstructor
public class Login {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int loginId;

  @Column(name = "email", nullable = false, length = 30)
  private String email;

  @Column(name = "password", length = 15)
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(name = "type", length = 10)
  private ChatType chatType;

  @OneToOne(mappedBy = "login", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name="user_id")
  private User user;

  @OneToOne(mappedBy = "login", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Admin admin;

  @OneToOne(mappedBy = "login", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Restaurant restaurant;

  @PrePersist
  @PreUpdate
  public void setChatType() {
    if (user != null) {
      this.chatType = ChatType.USER;
    } else if (admin != null) {
      // Assuming Admin entity exists
      this.chatType = ChatType.ADMIN;
    } else if (restaurant != null) {
      // Assuming Restaurant entity exists
      this.chatType = ChatType.RESTAURANT;
    }
  }
}