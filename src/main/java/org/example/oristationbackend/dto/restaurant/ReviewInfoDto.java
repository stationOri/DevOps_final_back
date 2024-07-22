package org.example.oristationbackend.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReviewInfoDto {
    private int reviewId; // 리뷰 id
    private String userNickname; // 사용자 닉네임
    private double reviewGrade; // 리뷰 평점
    private String reviewData; // 리뷰 내용
    private String reviewImg; // 리뷰 이미지1
    private String reviewImg2; // 리뷰 이미지2
    private String reviewImg3; // 리뷰 이미지3
    private Timestamp reviewDate; // 리뷰 작성일
    private int likeNum; // 좋아요 수
    private boolean blind; // 블라인드 여부
}
