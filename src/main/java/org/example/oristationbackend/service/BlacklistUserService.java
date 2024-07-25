package org.example.oristationbackend.service;

import lombok.RequiredArgsConstructor;
import org.example.oristationbackend.dto.admin.UserBlackResDto;
import org.example.oristationbackend.repository.BlacklistUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BlacklistUserService {
  private final BlacklistUserRepository blacklistUserRepository;
  private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

  // 사용자 블랙리스트 전체 목록 조회
  public List<UserBlackResDto> findAllUserBlacklist() {
    return blacklistUserRepository.findAll().stream()
        .map(blacklistUser -> new UserBlackResDto(
            blacklistUser.getBlacklistUserId(),
            formatTimestamp(blacklistUser.getProcessingDate()),
            blacklistUser.getUser().getLogin().getEmail(),
            blacklistUser.getAdmin().getLogin().getEmail(),
            blacklistUser.getBlackStatus().getDescription(),
            blacklistUser.getReportNum()
        ))
        .collect(Collectors.toList());
  }

  private String formatTimestamp(Timestamp timestamp) {
    return timestamp != null ? DATE_FORMAT.format(timestamp) : null;
  }
}
