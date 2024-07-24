package org.example.oristationbackend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.oristationbackend.dto.restaurant.MenuDto;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationReqDto {
    private int restId;
    private int userId;
    private String selectedTime;
    private String selectedDate;
    private int resNum;
    private int deposit;
    private String request;
    private List<MenuDto> menulist;
}

