package org.example.oristationbackend.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.oristationbackend.entity.type.ReservationStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationUpdateDto {
    private ReservationStatus status;
    private String reason;
}
