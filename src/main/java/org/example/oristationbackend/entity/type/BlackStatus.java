package org.example.oristationbackend.entity.type;

import lombok.Getter;

public enum BlackStatus {
  A("정지 대기"),
  B("정지중"),
  C("1회 경고 완료"),
  D("탈퇴 완료");

  @Getter
  private final String description;

  BlackStatus(String description) {
    this.description = description;
  }

}
