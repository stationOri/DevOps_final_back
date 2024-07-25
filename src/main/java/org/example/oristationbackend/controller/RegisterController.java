package org.example.oristationbackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.restaurant.RestRegisterDto;
import org.example.oristationbackend.dto.user.UserRegisterReqDto;
import org.example.oristationbackend.repository.LoginRepository;
import org.example.oristationbackend.repository.UserRepository;
import org.example.oristationbackend.service.RestaurantService;
import org.example.oristationbackend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {
    private final UserService userService;
    private final RestaurantService restaurantService;

    // 사용자 회원가입
    @PostMapping("/user")
    public int userRegister(@RequestBody UserRegisterReqDto userRegisterReqDto) {
        return userService.addUser(userRegisterReqDto);
    }

    //
    @PostMapping("/restaurant")
    public ResponseEntity<?> registerRestaurant(
        @ModelAttribute RestRegisterDto restRegisterDto,
        @RequestParam("file") MultipartFile file) {
        try {
            int result = restaurantService.addRestaurant(restRegisterDto, file);
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("파일 업로드 실패");
        }
    }
}
