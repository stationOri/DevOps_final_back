package org.example.oristationbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "keyword")
@NoArgsConstructor
@AllArgsConstructor
public class Keyword {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int keyword_id;

  @Column(length=10, nullable=false)
  private String keyword;
}
