package org.poc.jooq_retrofit.controller;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.poc.jooq_retrofit.controller.model.CreateCheckoutRequest;
import org.poc.jooq_retrofit.controller.model.GetCheckoutResponce;
import org.poc.jooq_retrofit.service.CheckoutService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CheckoutController {

  private final CheckoutService checkoutService;

  @PostMapping("/checkout")
  public void createCheckout(@RequestBody final CreateCheckoutRequest checkoutDto) {
    checkoutService.saveCheckout(checkoutDto.toCheckoutDto());
  }

  @GetMapping("/checkout/{checkoutId}")
  public GetCheckoutResponce getCheckout(@PathVariable final UUID checkoutId) {
    return GetCheckoutResponce.from(checkoutService.getCheckout(checkoutId));
  }
}
