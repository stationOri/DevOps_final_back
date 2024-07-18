package org.example.oristationbackend.securiity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.oristationbackend.dto.user.LoginDto;
import org.example.oristationbackend.dto.user.RegisterDto;
import org.example.oristationbackend.entity.type.ChatType;

@Data
@AllArgsConstructor
public class LoginWrapper {
    private LoginDto loginDto;
    private RegisterDto registerDto;
    public boolean hasRegistered(){
        return this.loginDto != null;
    }
    public ChatType whatType(){
        return this.loginDto.getChatType();
    }
}
