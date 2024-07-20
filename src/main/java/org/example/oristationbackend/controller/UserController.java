package org.example.oristationbackend.controller;

import org.example.oristationbackend.dto.user.UserInfoModReqDto;
import org.example.oristationbackend.dto.user.UserInfoResDto;
import org.example.oristationbackend.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  // 사용자 정보 조회 - 전체 사용자 조회
  @GetMapping
  public List<UserInfoResDto> getAllUsers() {
    return userService.getAllUsers();
  }

  // 사용자 정보 조회 - 사용자 id로 조회
  @GetMapping("/{userId}")
  public UserInfoResDto getUserById(@PathVariable(name = "userId") int userId) {
    return userService.getUserById(userId);
  }

  // 사용자 정보 수정
  @PutMapping("/{userId}")
  public UserInfoResDto updateUser(@PathVariable(name = "userId") int userId, @RequestBody UserInfoModReqDto userInfoModReqDto) {
    return userService.updateUser(userId, userInfoModReqDto);
  }

}
