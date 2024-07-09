package org.example.oristationbackend.entity.type;

import lombok.Getter;

public enum chatType {
  USER("사용자"),
  ADMIN("관리자"),
  RESTAURANT("식당");

  @Getter
  private final String description;

  chatType(String description) {
    this.description = description;
  }

}
