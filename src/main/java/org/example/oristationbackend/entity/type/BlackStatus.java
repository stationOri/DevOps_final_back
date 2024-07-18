package org.example.oristationbackend.entity.type;

import lombok.Getter;

public enum BlackStatus {
  A("정지대기"),
  B("정지중"),
  C("1회경고완료"),
  D("탈퇴완료");

  @Getter
  private final String description;

  BlackStatus(String description) {
    this.description = description;
  }

}
