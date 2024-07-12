package org.example.oristationbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.user.ChatMessageDto;
import org.example.oristationbackend.dto.user.ChatRoomDto;
import org.example.oristationbackend.dto.user.SendMessageDto;
import org.example.oristationbackend.entity.ChatRoom;
import org.example.oristationbackend.entity.Message;
import org.example.oristationbackend.repository.ChatRoomRepository;
import org.example.oristationbackend.repository.LoginRepository;
import org.example.oristationbackend.repository.MessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;
    private final MessageRepository messageRepository;
    private final LoginRepository loginRepository;

    public List<ChatRoomDto> getChatRooms(int userId) {
        return chatRoomRepository.findChatRoomsByUserId(userId);
    }

    public List<ChatMessageDto> getChatMessages(int chatRoomId){
        return chatRoomRepository.findMessagesByChatRoomId(chatRoomId);
    }

    public int sendMessage(SendMessageDto sendMessageDto){
        Optional<ChatRoom> chatRoomOptional = chatRoomRepository.findByQuestioner_LoginIdAndAnswerer_LoginId(sendMessageDto.getSenderId(), sendMessageDto.getReceiverId());

        if (!chatRoomOptional.isPresent()) {
            chatRoomOptional = chatRoomRepository.findByQuestioner_LoginIdAndAnswerer_LoginId(sendMessageDto.getReceiverId(), sendMessageDto.getSenderId());
        }

        int roomId = chatRoomOptional.map(ChatRoom::getChattingRoomId)
                .orElseGet(() -> chatRoomRepository.save(new ChatRoom(0,
                                loginRepository.findById(sendMessageDto.getSenderId()).orElseThrow(() -> new IllegalArgumentException("Sender not found")),
                                loginRepository.findById(sendMessageDto.getReceiverId()).orElseThrow(() -> new IllegalArgumentException("Receiver not found")),
                                null))
                        .getChattingRoomId());


        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new IllegalArgumentException("ChatRoom not found"));
        return messageRepository.save(new Message(0,sendMessageDto.getSenderId(), sendMessageDto.getMessageContent(),
                sendMessageDto.getSendTime(),chatRoom)).getMessageId();

    }
}
