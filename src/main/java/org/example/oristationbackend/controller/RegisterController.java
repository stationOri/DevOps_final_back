package org.example.oristationbackend.controller;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.restaurant.RestRegisterDto;
import org.example.oristationbackend.dto.user.UserRegisterReqDto;
import org.example.oristationbackend.repository.LoginRepository;
import org.example.oristationbackend.repository.UserRepository;
import org.example.oristationbackend.service.RestaurantService;
import org.example.oristationbackend.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegisterController {
    private final UserService userService;
    private final RestaurantService restaurantService;

    @PostMapping("/user")
    public int userRegister(@RequestBody UserRegisterReqDto userRegisterReqDto) {
        return userService.addUser(userRegisterReqDto);
    }
    @PostMapping("/restaurant")
    public int restRegister(@RequestBody RestRegisterDto restRegisterDto) {
        return restaurantService.addRestaurant(restRegisterDto);
    }
}
