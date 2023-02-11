package org.poc.jooq_retrofit;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.put;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static io.restassured.RestAssured.given;

import com.github.tomakehurst.wiremock.http.Body;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

public class CheckoutIT extends BaseIntegrationTest {

  @Test
  void given3rdPartyServiceUnavailable_whenCreateCheckout_thenFallback() {
    given()
        .contentType(ContentType.JSON)
        .body(
            """
              {
                  "description": "Description for checkout",
                  "status": "failed"
              }
              """)
        .when()
        .post("/checkout")
        .then()
        .statusCode(200);
  }

  @Test
  void given3rdPartyServiceAvailable_whenCreateCheckout_thenUpdate3rdPartyService() {
    stubFor(
        put(urlEqualTo("/notification"))
            .willReturn(
                aResponse()
                    .withResponseBody(
                        Body.fromJsonBytes(
                            """
            {"result": "ok"}
            """.getBytes()))));
    given()
        .contentType(ContentType.JSON)
        .body(
            """
                        {
                            "description": "Description for checkout",
                            "status": "failed"
                         }""")
        .when()
        .post("/checkout")
        .then()
        .statusCode(200);
  }
}
