package org.example.oristationbackend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SendMessageDto {
    private int receiverId;
    private int senderId;
    private String messageContent;
    private Timestamp sendTime;
}
