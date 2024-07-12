package org.example.oristationbackend.repository;

import org.example.oristationbackend.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Integer>, CustomChatRoomRepository {
    Optional<ChatRoom> findByQuestioner_LoginIdAndAnswerer_LoginId(int questionerId, int answererId);

}
