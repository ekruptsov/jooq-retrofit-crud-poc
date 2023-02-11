package org.poc.jooq_retrofit.controller.model;

import java.time.OffsetDateTime;
import java.util.UUID;
import org.poc.jooq_retrofit.service.model.CheckoutDto;

public record GetCheckoutResponse(
    UUID id, String description, String status, OffsetDateTime created, OffsetDateTime updated) {

  public static GetCheckoutResponse from(CheckoutDto checkout) {
    return new GetCheckoutResponse(
        checkout.id(),
        checkout.description(),
        checkout.status(),
        checkout.created(),
        checkout.updated());
  }
}
