package org.example.oristationbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.user.UserInfoResDto;
import org.example.oristationbackend.dto.user.UserRegisterReqDto;
import org.example.oristationbackend.entity.Login;
import org.example.oristationbackend.entity.User;
import org.example.oristationbackend.entity.type.ChatType;
import org.example.oristationbackend.repository.LoginRepository;
import org.example.oristationbackend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final LoginRepository loginRepository;

    // 사용자 등록
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

    // 사용자 존재 여부 확인
    private boolean existUser(String phone) {
        return userRepository.existsByUserPhone(phone);
    }

    // 사용자 정보 수정


    // 사용자 정보 조회 - 전체 사용자 조회
    public List<UserInfoResDto> getAllUsers() {
        return userRepository.findAll().stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
    }

    // 사용자 정보 조회 - 사용자 id로 조회
    public UserInfoResDto getUserById(int userId) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDto(user);
    }

    // 엔티티를 DTO로 변환
    private UserInfoResDto convertToDto(User user) {
        return new UserInfoResDto(
            user.getUserId(),
            user.getUserName(),
            user.getUserNickname(),
            user.getUserPhone(),
            user.getLogin().getEmail(),
            user.isBlocked(),
            user.getQuitDate() != null
        );
    }
}