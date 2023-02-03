package org.poc.jooq_retrofit.communication;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.vavr.control.Try;
import lombok.extern.slf4j.Slf4j;
import org.poc.jooq_retrofit.communication.api.JsonApi;
import org.poc.jooq_retrofit.communication.api.XmlApi;
import org.poc.jooq_retrofit.communication.properties.ClientProperties;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;

@Slf4j
@Component
public class Client {

  private final JsonApi jsonApi;

  private final XmlApi xmlApi;

  private final RetryConfig retryConfig;

  public Client(JsonApi jsonApi, XmlApi xmlApi, ClientProperties properties) {
    this.jsonApi = jsonApi;
    this.xmlApi = xmlApi;
    retryConfig =
            RetryConfig.<Response<?>>custom()
                    .maxAttempts(5)
                    .waitDuration(Duration.ofMillis(properties.getWaitTimeout()))
                    .retryOnResult(response -> response.code() > 500 || response.code() == 406)
                    .retryExceptions(IOException.class, TimeoutException.class)
                    .build();
  }

  /**
   * Return deserialized JSON object.
   *
   * @param result acknowledged submission result
   * @return json string value
   */
  public String submitResult(String result) {
    return runWithRetry(
            "result_submission",
            () -> jsonApi.submitResult(result).execute(),
            throwable -> {
              log.error("submitResult err:", throwable);
              return Response.success("fallback msg");
            })
        .body();
  }

  /**
   * Return deserialized XML object.
   *
   * @return xml string value
   */
  public String getXml() {
    return runWithRetry(
            "getXml",
            () -> xmlApi.getXml().execute(),
            throwable -> Response.success("fallback result"))
        .body();
  }

  private <T> Response<T> runWithRetry(
      final String processName,
      final Callable<Response<T>> callable,
      final Function<? super Throwable, ? extends Response<T>> fallback) {
    return Try.ofCallable(Retry.decorateCallable(Retry.of(processName, retryConfig), callable))
        .onFailure(
            err ->
                log.warn(
                    "process={}, result=failure, "
                        + "message='all requests to service are failed, use fallback'",
                    processName))
        .recover(fallback)
        .get();
  }
}
