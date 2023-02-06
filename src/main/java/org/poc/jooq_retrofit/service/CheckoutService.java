package org.poc.jooq_retrofit.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.poc.jooq_retrofit.repository.CheckoutDao;
import org.poc.jooq_retrofit.service.model.CheckoutDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckoutService {

  private final External3rdPartyService external3rdPartyService;
  private final CheckoutDao checkoutDao;

  public void saveCheckout(final CheckoutDto checkout) {
    checkoutDao.persist(checkout.toCheckout());
    external3rdPartyService.updateNotification(checkout);
  }

  public CheckoutDto getCheckout(final UUID checkoutId) {
    return checkoutDao
        .retrieve(checkoutId)
        .map(CheckoutDto::from)
        .orElseThrow(() -> new IllegalArgumentException("checkout not found"));
  }
}
