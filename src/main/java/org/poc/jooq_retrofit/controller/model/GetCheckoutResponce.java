package org.poc.jooq_retrofit.controller.model;

import java.time.OffsetDateTime;
import java.util.UUID;
import org.poc.jooq_retrofit.service.model.CheckoutDto;

public record GetCheckoutResponce(
    UUID id, String description, String status, OffsetDateTime created, OffsetDateTime updated) {

  public static GetCheckoutResponce from(CheckoutDto checkout) {
    return new GetCheckoutResponce(
        checkout.id(),
        checkout.description(),
        checkout.status(),
        checkout.created(),
        checkout.updated());
  }
}
