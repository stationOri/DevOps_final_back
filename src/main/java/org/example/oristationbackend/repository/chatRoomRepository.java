package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.BlacklistUser;
import org.example.oristationbackend.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface chatRoomRepository extends JpaRepository<ChatRoom, Integer> {
}
