package org.poc.jooq_retrofit.controller.model;

import java.util.UUID;
import org.poc.jooq_retrofit.service.model.CheckoutDto;

public record CreateCheckoutRequest(String description, String status) {

  public CheckoutDto toCheckoutDto() {
    return new CheckoutDto(UUID.randomUUID(), description, status, null, null);
  }
}
