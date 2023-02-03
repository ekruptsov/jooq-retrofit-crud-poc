package org.template.test.assignment.communication.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.template.test.assignment.communication.api.JsonApi;
import org.template.test.assignment.communication.api.XmlApi;
import org.template.test.assignment.communication.properties.ApiProperties;

@AllArgsConstructor
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
