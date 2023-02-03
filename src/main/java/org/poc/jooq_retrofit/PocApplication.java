package org.poc.jooq_retrofit;

import org.poc.jooq_retrofit.communication.properties.ApiProperties;
import org.poc.jooq_retrofit.communication.properties.ClientProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties({ApiProperties.class, ClientProperties.class})
@SpringBootApplication
public class PocApplication {

  public static void main(String[] args) {
    SpringApplication.run(PocApplication.class, args);
  }
}
