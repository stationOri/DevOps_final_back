package org.example.oristationbackend.entity.type;

import lombok.Getter;

@Getter
public enum ChatType {
  USER("사용자"),
  ADMIN("관리자"),
  RESTAURANT("식당");

  private final String description;

  ChatType(String description) {
    this.description = description;
  }
}
