package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositoryChatRoom extends JpaRepository<ChatRoom, Integer>, CustomChatRoomRepository {
}
