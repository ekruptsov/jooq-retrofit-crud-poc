package org.poc.jooq_retrofit.service;

import lombok.RequiredArgsConstructor;
import org.poc.jooq_retrofit.communication.Client3rdParty;
import org.poc.jooq_retrofit.communication.model.Update3rdPartyRequest;
import org.poc.jooq_retrofit.service.model.CheckoutDto;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class External3rdPartyService {

  private final Client3rdParty client3rdParty;

  public void updateNotification(CheckoutDto checkout) {
    client3rdParty.submitUpdate(new Update3rdPartyRequest(checkout.id(), checkout.status()));
  }
}
