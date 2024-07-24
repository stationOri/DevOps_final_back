package org.example.oristationbackend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NearRestReviewDto {
  private int reviewId; // 리뷰 id
  private double reviewGrade; // 리뷰 별점
  private String userNickname; // 유저 닉네임
  private int likeNum; // 좋아요 수
  private String reviewData; // 리뷰 내용
}
