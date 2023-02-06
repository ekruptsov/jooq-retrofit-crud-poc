package org.poc.jooq_retrofit.service.model;

import java.time.OffsetDateTime;
import java.util.UUID;
import org.poc.jooq_retrofit.repository.jooq.tables.pojos.Checkout;

public record CheckoutDto(
    UUID id, String description, String status, OffsetDateTime created, OffsetDateTime updated) {

  public Checkout toCheckout() {
    return new Checkout(id, description, status, created, updated);
  }

  public static CheckoutDto from(Checkout checkout) {
    return new CheckoutDto(
        checkout.getId(),
        checkout.getDescription(),
        checkout.getStatus(),
        checkout.getCreated(),
        checkout.getUpdated());
  }
}
