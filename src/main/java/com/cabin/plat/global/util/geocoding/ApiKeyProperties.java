package com.cabin.plat.global.util.geocoding;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "api-key")
public class ApiKeyProperties {
    private String clientId;
    private String clientKey;
}