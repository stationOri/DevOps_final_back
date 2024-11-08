package org.example.oristationbackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
  private int keywordId;

  @Column(length=10, nullable=false)
  private String keyword;
}
