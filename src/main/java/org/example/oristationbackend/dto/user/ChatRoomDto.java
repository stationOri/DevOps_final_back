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
    private int chattingRoomId;
    private  String qsName;
    private int qsId;
    private String ansName;
    private int ansId;
    private String lastMsg;
}

