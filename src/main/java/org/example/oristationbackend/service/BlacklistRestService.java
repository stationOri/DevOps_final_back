package org.example.oristationbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.admin.RestBlackResDto;
import org.example.oristationbackend.repository.BlacklistRestRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BlacklistRestService {
  private final BlacklistRestRepository blacklistRestRepository;
  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

  // 식당 블랙리스트 전체 목록 조회
  public List<RestBlackResDto> findAllRestBlacklist() {
    return blacklistRestRepository.findAll().stream()
        .map(blacklistRest -> new RestBlackResDto(
            blacklistRest.getBlacklistRestId(),
            formatTimestamp(blacklistRest.getProcessingDate()), // Timestamp를 문자열로 포맷
            blacklistRest.getRestaurant().getLogin().getEmail(),
            blacklistRest.getAdmin().getLogin().getEmail(),
            blacklistRest.getBlackStatus().getDescription(),
            blacklistRest.getReportNum()
        ))
        .collect(Collectors.toList());
  }

  private String formatTimestamp(Timestamp timestamp) {
    return timestamp != null ? DATE_FORMAT.format(timestamp) : null;
  }
}