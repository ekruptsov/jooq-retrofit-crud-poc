package org.poc.jooq_retrofit.communication.properties;

import jakarta.validation.constraints.NotNull;
import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "client")
@Value
public class ClientProperties {

  @NotNull Integer waitTimeout;
}
