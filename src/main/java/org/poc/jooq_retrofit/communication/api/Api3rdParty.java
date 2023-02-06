package org.poc.jooq_retrofit.communication.api;

import org.poc.jooq_retrofit.communication.model.Update3rdPartyRequest;
import org.poc.jooq_retrofit.communication.model.Update3rdPartyResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.PUT;

public interface Api3rdParty {

  @PUT("/notification")
  Call<Update3rdPartyResponse> submitUpdate(@Body Update3rdPartyRequest result);
}
