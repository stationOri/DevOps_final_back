package org.example.oristationbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.entity.Keyword;
import org.example.oristationbackend.repository.KeywordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class KeywordService {
  private final KeywordRepository keywordRepository;

  public List<Keyword> getAllKeywords() {
    return keywordRepository.findAll();
  }
}
