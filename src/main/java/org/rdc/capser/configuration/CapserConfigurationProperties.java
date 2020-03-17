package org.rdc.capser.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "capser")
@Data
public class CapserConfigurationProperties {

    private double inactivityTime;

    private int placementMatchesNumber;

    private boolean cloud = false;

    private float buildNumber = 2.01f;
}
