package org.poc.jooq_retrofit.repository;

import static org.poc.jooq_retrofit.repository.jooq.Tables.CHECKOUT;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import org.poc.jooq_retrofit.repository.jooq.tables.pojos.Checkout;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CheckoutDao {

  private final DSLContext jooq;

  public void persist(final Checkout checkout) {
    jooq.insertInto(CHECKOUT)
        .set(CHECKOUT.DESCRIPTION, checkout.getDescription())
        .set(CHECKOUT.STATUS, checkout.getStatus())
        .set(CHECKOUT.ID, checkout.getId())
        .execute();
  }

  public @NotNull Optional<Checkout> retrieve(final UUID checkoutId) {
    return jooq.selectFrom(CHECKOUT)
        .where(CHECKOUT.ID.eq(checkoutId))
        .fetchOptionalInto(Checkout.class);
  }
}
