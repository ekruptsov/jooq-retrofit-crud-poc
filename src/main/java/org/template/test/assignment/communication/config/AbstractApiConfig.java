package org.template.test.assignment.communication.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.retrofit.CircuitBreakerCallAdapter;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public abstract class AbstractApiConfig {

  <T> T provideApi(String url, Class<T> apiClass, ObjectMapper objectMapper) {
    return createResilientRetrofit(
            url, createCircuitBreaker(apiClass.getSimpleName()), objectMapper)
        .create(apiClass);
  }

  protected CircuitBreaker createCircuitBreaker(String circuitBreakerName) {
    return CircuitBreaker.of(
        circuitBreakerName + "-circuit-breaker-" + System.currentTimeMillis(),
        CircuitBreakerConfig.custom().enableAutomaticTransitionFromOpenToHalfOpen().build());
  }

  protected Retrofit createResilientRetrofit(
      String url, CircuitBreaker circuitBreaker, ObjectMapper objectMapper) {
    return new Retrofit.Builder()
        .addCallAdapterFactory(
            CircuitBreakerCallAdapter.of(circuitBreaker, response -> response.code() < 500))
        .addConverterFactory(JacksonConverterFactory.create(objectMapper))
        .baseUrl(url)
        .build();
  }
}
