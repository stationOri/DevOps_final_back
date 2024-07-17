package org.example.oristationbackend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WaitingReqDto {
    private int userId;
    private int restId;
    private int waitingPpl;
    private String watingPhone;
}
