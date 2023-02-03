package org.template.test.assignment.communication;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.vavr.control.Try;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.template.test.assignment.communication.api.JsonApi;
import org.template.test.assignment.communication.api.XmlApi;
import org.template.test.assignment.communication.properties.ClientProperties;
import retrofit2.Response;

@Slf4j
@RequiredArgsConstructor
@Component
public class Client {

  private final JsonApi jsonApi;

  private final XmlApi xmlApi;

  private final ClientProperties properties;

  private RetryConfig retryConfig;

  @PostConstruct
  private void init() {
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
      String processName,
      Callable<Response<T>> callable,
      Function<? super Throwable, ? extends Response<T>> fallback) {
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
