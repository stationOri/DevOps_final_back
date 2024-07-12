package org.example.oristationbackend.dto.user;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChatRoomDto {
    int chattimgRoomId;
    String qsName;
    String ansName;
    String lastMsg;
}

