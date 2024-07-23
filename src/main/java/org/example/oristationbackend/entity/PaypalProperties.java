package org.example.oristationbackend.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "paypal")
public class PaypalProperties {

    private String baseUrl;
    private String clientId;
    private String secret;
}