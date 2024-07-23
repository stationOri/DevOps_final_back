package org.example.oristationbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.user.LoginDto;

import org.example.oristationbackend.dto.user.RegisterDto;
import org.example.oristationbackend.entity.Admin;
import org.example.oristationbackend.entity.Login;
import org.example.oristationbackend.entity.Restaurant;
import org.example.oristationbackend.entity.User;
import org.example.oristationbackend.entity.type.ChatType;
import org.example.oristationbackend.entity.type.RestaurantStatus;
import org.example.oristationbackend.repository.AdminRepository;
import org.example.oristationbackend.repository.LoginRepository;
import org.example.oristationbackend.repository.RestaurantRepository;
import org.example.oristationbackend.repository.UserRepository;
import org.example.oristationbackend.securiity.JwtUtil;
import org.example.oristationbackend.securiity.LoginWrapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LoginService {
    private final LoginRepository loginRepository;
    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;
    private final AdminRepository adminRepository;
    public LoginWrapper checkRegister(RegisterDto registerDto){
        String phone= registerDto.getPhone();
        Optional<User> user=userRepository.findByUserPhone(phone);
        Optional<Restaurant> restaurant=restaurantRepository.findByRestPhone(phone);
        Optional<Admin> admin=adminRepository.findByAdminPhone(phone);
//        if(user.isPresent()){
//            return new LoginWrapper(new LoginDto(user.get().getUserId(), ChatType.USER),null);
//        }else if(restaurant.isPresent()){
//            return new LoginWrapper(new LoginDto(restaurant.get().getRestId(), ChatType.RESTAURANT),null);
//        } else if (admin.isPresent()) {
//            return new LoginWrapper(new LoginDto(admin.get().getAdminId(), ChatType.ADMIN),null);
//        }else{
//            return new LoginWrapper(null,registerDto);
//        }
        return user.map(u -> new LoginWrapper(new LoginDto(u.getUserId(), ChatType.USER), null))
                .or(() -> restaurant.map(r -> new LoginWrapper(new LoginDto(r.getRestId(), ChatType.RESTAURANT), null)))
                .or(() -> admin.map(a -> new LoginWrapper(new LoginDto(a.getAdminId(), ChatType.ADMIN), null)))
                .orElseGet(() -> new LoginWrapper(null, registerDto));


    }
    @Value("${jwt.secret}")
    private String secretKey;
    private Long expiredMs = 1000 * 60 * 60L;
    public String genJwtToken(String username,Object object) {
        return JwtUtil.createJwt(username, object, secretKey, expiredMs);
    }
    public String LoginOk(LoginDto loginDto){
        switch (loginDto.getChatType()){
            case RESTAURANT:
                Restaurant restaurant=restaurantRepository.findById(loginDto.getId()).get();
                if(restaurant.isBlocked()){
                    return "1";
                }else if(restaurant.getRestStatus()== RestaurantStatus.A){
                    return "2";
                }
                break;
            case USER:
                User user=userRepository.findById(loginDto.getId()).get();
                if(user.isBlocked()){
                    return "3";
                }
                break;
            default:
                return null;
        }
        return null;
    }
}
