package org.poc.jooq_retrofit.communication.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.poc.jooq_retrofit.communication.api.Api3rdParty;
import org.poc.jooq_retrofit.communication.properties.ApiProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class ApiConfig extends AbstractApiConfig {

  private final ObjectMapper objectMapper;
  private final ApiProperties properties;

  @Bean
  public Api3rdParty provideFixtureJsonApi() {
    return provideApi(properties.getUrl(), Api3rdParty.class, objectMapper);
  }
}
