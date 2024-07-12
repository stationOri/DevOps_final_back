package org.example.oristationbackend.repository;

import org.example.oristationbackend.dto.user.ChatMessageDto;
import org.example.oristationbackend.dto.user.ChatRoomDto;

import java.util.List;

public interface CustomChatRoomRepository {
    List<ChatRoomDto> findChatRoomsByUserId(int userId);
    List<ChatMessageDto> findMessagesByChatRoomId(int chatroomId);
}
