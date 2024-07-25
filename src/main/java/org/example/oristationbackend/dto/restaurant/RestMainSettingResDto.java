package org.example.oristationbackend.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RestMainSettingResDto {
    private String restName;
    private String restPhoto;
    private String restAddress; // 식당 주소
    private String restIntro; // 식당 소개
    private String restPhone; // 식당 전화번호
}
