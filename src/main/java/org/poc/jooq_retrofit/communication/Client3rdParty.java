package org.poc.jooq_retrofit.communication;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.vavr.control.Try;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.poc.jooq_retrofit.communication.api.Api3rdParty;
import org.poc.jooq_retrofit.communication.model.Update3rdPartyRequest;
import org.poc.jooq_retrofit.communication.model.Update3rdPartyResponse;
import org.poc.jooq_retrofit.communication.properties.ClientProperties;
import org.springframework.stereotype.Component;
import retrofit2.Response;

@Slf4j
@Component
public class Client3rdParty {

  private final Api3rdParty jsonApi;

  private final RetryConfig retryConfig;

  public Client3rdParty(
      final Api3rdParty jsonApi, ObjectMapper objectMapper, final ClientProperties properties) {
    this.jsonApi = jsonApi;
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
   * @param request acknowledged submission request
   * @return json mapped value
   */
  public Update3rdPartyResponse submitUpdate(final Update3rdPartyRequest request) {
    return runWithRetry(
            "update_3rd_party",
            () -> jsonApi.submitUpdate(request).execute(),
            throwable -> {
              log.error("submitResult err:", throwable);
              return createFallbackMessage();
            })
        .body();
  }

  private Response<Update3rdPartyResponse> createFallbackMessage() {
    return Response.success(new Update3rdPartyResponse("fallback"));
  }

  /**
   * Return deserialized XML object.
   *
   * @return xml string value
   */
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
