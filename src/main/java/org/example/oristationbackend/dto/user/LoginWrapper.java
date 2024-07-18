package org.example.oristationbackend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginWrapper {
    private LoginDto loginDto;
    private RegisterDto registerDto;
}
