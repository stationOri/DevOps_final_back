package org.example.oristationbackend.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.oristationbackend.entity.ReservedMenu;
import org.example.oristationbackend.entity.type.ReservationStatus;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestReservationResDto {
    private String userName;
    private int userId;
    private String restName;
    private int restaurantId;
    private int reservationId;
    private Timestamp resDate;
    private Timestamp reqDate;
    private int resNum;
    private ReservationStatus status;
    private String request;
    private Timestamp statusChangeDate;
    private List<ReservedMenu> menuList;
}
