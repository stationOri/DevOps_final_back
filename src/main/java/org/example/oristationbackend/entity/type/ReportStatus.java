package org.example.oristationbackend.entity.type;

import lombok.Getter;

public enum ReportStatus {
  A("처리대기"),
  B("반려"),
  C("승인");

  @Getter
  private final String description;

  ReportStatus(String description) {
    this.description = description;
  }
}
