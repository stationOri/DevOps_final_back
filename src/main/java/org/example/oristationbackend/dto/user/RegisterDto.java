package org.example.oristationbackend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RegisterDto {
    private String userName;
    private String nickName;
    private String email;
    private String phone;
    public static String changeNumber(String phone){
         return phone.replaceAll("-", "");
    }
}
