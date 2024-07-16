package org.example.oristationbackend.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.oristationbackend.entity.type.UserWaitingStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WaitingRestResDto {
    private int waitingId;
    private int waitingNum;
    private int waitingPpl;
    private String userName;
    private UserWaitingStatus waitingStatus;
}
