package org.example.oristationbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.user.LoginDto;
import org.example.oristationbackend.dto.user.LoginWrapper;
import org.example.oristationbackend.dto.user.RegisterDto;
import org.example.oristationbackend.entity.Login;
import org.example.oristationbackend.repository.LoginRepository;
import org.example.oristationbackend.securiity.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoginService {
    private final LoginRepository loginRepository;
    public Object checkRegister(RegisterDto registerDto){
        Optional<Login> login=loginRepository.findByEmail(registerDto.getEmail());
        if(login.isPresent()){
            return new LoginDto(login.get().getLoginId(),login.get().getChatType());
        }else{
            return new LoginWrapper(null,registerDto);
        }
    }
    @Value("${jwt.secret}")
    private String secretKey;
    private Long expiredMs = 1000 * 60 * 60L;
    public String genJwtToken(String username,Object object) {
        return JwtUtil.createJwt(username,object, secretKey, expiredMs);
    }
}
