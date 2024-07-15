package org.example.oristationbackend.dto.user;

import lombok.*;

@Data
public class EmailDto {
    private String mail;
    private String verifyCode;
}
