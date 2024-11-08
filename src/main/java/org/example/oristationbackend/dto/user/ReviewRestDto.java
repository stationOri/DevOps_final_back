package org.example.oristationbackend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRestDto {
    private int reviewId;
    private String userNickname;
    private double reviewGrade;
    private String reviewData;
    private String reviewImg;
    private String reviewImg2;
    private String reviewImg3;
    private Timestamp reviewDate;
    private int likeNum;
    private boolean blind;
    private boolean likedByUser;

}
