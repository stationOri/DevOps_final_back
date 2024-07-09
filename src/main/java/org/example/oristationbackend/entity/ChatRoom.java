package org.example.oristationbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Table(name = "chatting_room")
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int chattingRoomId;

  @ManyToOne
  @JoinColumn(name = "questioner_id", nullable = false)
  private Login questioner;

  @ManyToOne
  @JoinColumn(name = "answerer_id", nullable = false)
  private Login answerer;

  @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<Message> messages;
}