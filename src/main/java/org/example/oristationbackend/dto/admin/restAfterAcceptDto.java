package org.example.oristationbackend.dto.admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.oristationbackend.entity.type.RestaurantStatus;

import java.sql.Date;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class restAfterAcceptDto {
    private int rest_id;
    private String rest_name;
    private String rest_status;
    private String rest_num;
    private String rest_owner;
    private String rest_phone;
    private String rest_data;
    private String join_date;
    private String quit_date;
    private boolean rest_isblocked;
    private boolean rest_isopen;
}
