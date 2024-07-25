package org.example.oristationbackend.controller;


import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.admin.RestBlackResDto;
import org.example.oristationbackend.dto.admin.UserBlackResDto;
import org.example.oristationbackend.service.BlacklistRestService;
import org.example.oristationbackend.service.BlacklistUserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/black")
@RequiredArgsConstructor
public class BlacklistController {
  private final BlacklistUserService blacklistUserService;
  private final BlacklistRestService blacklistRestService;

  // 사용자 블랙리스트 전체 목록 조회
  @GetMapping("/user")
  public List<UserBlackResDto> findAllUserBlacklist() {
    return blacklistUserService.findAllUserBlacklist();
  }

  // 식당 블랙리스트 전체 목록 조회
  @GetMapping("/rest")
  public List<RestBlackResDto> findAllRestBlacklist() {
    return blacklistRestService.findAllRestBlacklist();
  }
}
