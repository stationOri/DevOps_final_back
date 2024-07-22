package org.example.oristationbackend.dto.restaurant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestRegisterDto {
    private String email;
    private String restName;
    private String restPhone;
    private String restName2;
    private String restData;
    private String restImage;


}
