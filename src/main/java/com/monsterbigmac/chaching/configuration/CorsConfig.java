package com.monsterbigmac.chaching.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Setter
@Getter
@ConfigurationProperties
@Component
public class CorsConfig {

    private List<String> corsAllowedOrigins;

}
