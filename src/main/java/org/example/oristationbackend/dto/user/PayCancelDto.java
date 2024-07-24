package org.example.oristationbackend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayCancelDto {
    String reason;
    String imp_uid;
    String merchant_uid;
    int amount;
    Long checksum;
}
