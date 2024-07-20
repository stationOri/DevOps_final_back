package org.example.oristationbackend.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.oristationbackend.entity.type.RestaurantStatus;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class restAcceptReadyDto {
    private int restId;
    private String rest_name;
    private RestaurantStatus rest_status;
    private String rest_num;
    private String rest_owner;
    private String rest_phone;
    private String rest_data;
    private String join_date;
}
