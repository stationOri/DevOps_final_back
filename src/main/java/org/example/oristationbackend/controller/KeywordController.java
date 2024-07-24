package org.example.oristationbackend.controller;

import org.example.oristationbackend.dto.restaurant.KeywordResponseDto;
import org.example.oristationbackend.entity.Keyword;
import org.example.oristationbackend.service.KeywordService;
import org.example.oristationbackend.service.RestaurantInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/keywords")
public class KeywordController {
  private final KeywordService keywordService;
  private final RestaurantInfoService restaurantInfoService;;

  @Autowired
  public KeywordController(KeywordService keywordService, RestaurantInfoService restaurantInfoService) {
    this.keywordService = keywordService;
    this.restaurantInfoService = restaurantInfoService;
  }

  // 키워드 전체 조회
  @GetMapping
  public ResponseEntity<List<Keyword>> getAllKeywords() {
    List<Keyword> keywords = keywordService.getAllKeywords();
    if (keywords.isEmpty()) {
      return ResponseEntity.notFound().build();
    } else {
      return ResponseEntity.ok(keywords);
    }
  }

  //식당id로 키워드 조회
  @GetMapping("/{restId}")
  public List<KeywordResponseDto> getKeywordById(@PathVariable("restId") int restId) {
    return restaurantInfoService.findkeywordByRestId(restId);
  }

  //키워드 등록
  @PostMapping("{keyId}/rest/{restId}")
  public int addKeyword(@PathVariable("keyId") int keyId, @PathVariable("restId") int restId) {
    return restaurantInfoService.enrollkeywordByRestId(restId, keyId);
  }

  //키워드 삭제
  @DeleteMapping("/{keyId}/rest/{restId}")
  public int deleteKeyword(@PathVariable("keyId") int keyId, @PathVariable("restId") int restId) {
    return restaurantInfoService.deleteKeywordByRestId(restId, keyId);
  }

}
