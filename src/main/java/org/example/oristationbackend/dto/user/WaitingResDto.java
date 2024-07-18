package org.example.oristationbackend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.oristationbackend.entity.type.UserWaitingStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WaitingResDto {
    private int waitingId;
    private int waitingNum;
    private int waitingLeft;
    private int waitingPpl;

    private UserWaitingStatus waitingStatus;
    private String restName;
}
