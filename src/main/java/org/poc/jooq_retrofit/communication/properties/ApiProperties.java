package org.poc.jooq_retrofit.communication.properties;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "api")
@Value
public class ApiProperties {
  @NotBlank String url;
}
