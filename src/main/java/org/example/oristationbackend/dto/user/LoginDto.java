package org.example.oristationbackend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.example.oristationbackend.entity.type.ChatType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginDto {
    private int id;
    private ChatType chatType;
}
