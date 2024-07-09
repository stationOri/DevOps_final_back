package org.example.oristationbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "message")
@AllArgsConstructor
@NoArgsConstructor
public class Message {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int messageId;

  @Column(name = "sender_id", nullable = false)
  private int senderId;

  @Column(name = "message_content", nullable = false, length = 1000)
  private String messageContent;

  @Column(name = "send_time", nullable = false)
  private Timestamp sendTime;

  @ManyToOne
  @JoinColumn(name = "chatting_room_id", nullable = false)
  private ChatRoom chatRoom;
}
