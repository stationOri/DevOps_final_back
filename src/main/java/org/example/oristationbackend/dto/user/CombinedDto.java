package org.example.oristationbackend.dto.user;

import lombok.Data;

@Data
public class CombinedDto {
    ReservationReqDto reservationReqDto;
    PayDto payDto;
}
