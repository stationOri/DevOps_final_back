package org.example.oristationbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.user.UserRegisterReqDto;
import org.example.oristationbackend.entity.Login;
import org.example.oristationbackend.entity.User;
import org.example.oristationbackend.entity.type.ChatType;
import org.example.oristationbackend.repository.LoginRepository;
import org.example.oristationbackend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final LoginRepository loginRepository;
    @Transactional(readOnly = false)
    public int addUser(UserRegisterReqDto userRegisterReqDto) {
        if(existUser(userRegisterReqDto.getUserPhone())){
            return 0;
        }
        Login login = new Login(0, userRegisterReqDto.getEmail(), null, ChatType.USER, null, null, null);
        login = loginRepository.save(login);
        User user = new User(login, 0, userRegisterReqDto.getUserName(), userRegisterReqDto.getUserNickname(), userRegisterReqDto.getUserPhone(), false, new Date(System.currentTimeMillis()), null);
        user = userRepository.save(user);
        login.setUser(user);
        return loginRepository.save(login).getLoginId();
    }
    private boolean existUser(String phone) {
        return userRepository.existsByUserPhone(phone);
    }
}
