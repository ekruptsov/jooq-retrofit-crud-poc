package org.poc.jooq_retrofit.communication.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.poc.jooq_retrofit.communication.api.JsonApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.poc.jooq_retrofit.communication.api.XmlApi;
import org.poc.jooq_retrofit.communication.properties.ApiProperties;

@RequiredArgsConstructor
@Configuration
public class ApiConfig extends AbstractApiConfig {

  private final ApiProperties properties;

  @Bean
  public XmlApi provideFixtureXmlApi() {
    return provideApi(properties.getUrl(), XmlApi.class, new XmlMapper());
  }

  @Bean
  public JsonApi provideFixtureJsonApi() {
    return provideApi(properties.getUrl(), JsonApi.class, new ObjectMapper());
  }
}
