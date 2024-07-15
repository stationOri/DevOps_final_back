package org.example.oristationbackend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewReqDto {
    private int userId;
    private int restId;
    private String reviewData;
    private double reviewGrade;
    private String reviewImg;
    private String reviewImg2;
    private String reviewImg3;

}
