package org.example.oristationbackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.user.ChatMessageDto;
import org.example.oristationbackend.dto.user.ChatRoomDto;
import org.example.oristationbackend.dto.user.SendMessageDto;
import org.example.oristationbackend.service.ChatRoomService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatRoomService chatRoomService;
    @GetMapping("/user/{userId}")
    public List<ChatRoomDto> getChatRooms(@PathVariable("userId") int userId) {
        return chatRoomService.getChatRooms(userId);
    }
    @GetMapping("/chat/{chattingRoomId}")
    public List<ChatMessageDto> getChatMessages(@PathVariable("chattingRoomId") int chattingRoomId) {
        return chatRoomService.getChatMessages(chattingRoomId);
    }
    @PostMapping("/chat")
    public int sendMessage(@RequestBody SendMessageDto sendMessageDto ) {
        return chatRoomService.sendMessage(sendMessageDto);
    }
}
