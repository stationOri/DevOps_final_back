package org.example.oristationbackend.controller;

import org.example.oristationbackend.entity.Keyword;
import org.example.oristationbackend.service.KeywordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/keywords")
public class KeywordController {
  private final KeywordService keywordService;

  @Autowired
  public KeywordController(KeywordService keywordService) {
    this.keywordService = keywordService;
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

}
