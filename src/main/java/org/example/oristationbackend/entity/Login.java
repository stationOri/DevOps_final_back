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
  private int loginId; // 로그인 id

  @Column(name = "email", nullable = false, length = 30)
  private String email; // 이메일

  @Column(name = "password", length = 15)
  private String password; // 비밀번호

  @Enumerated(EnumType.STRING)
  @Column(name = "type", length = 10)
  private ChatType chatType; // 사용자 유형

  @OneToOne(mappedBy = "login", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  @JoinColumn(name="user_id")
  private User user; // 사용자 id

  @OneToOne(mappedBy = "login", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Admin admin; // 관리자 id

  @OneToOne(mappedBy = "login", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private Restaurant restaurant; // 식당 id

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