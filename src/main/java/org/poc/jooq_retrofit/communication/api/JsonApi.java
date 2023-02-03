package org.poc.jooq_retrofit.communication.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface JsonApi {

  @POST("/json")
  Call<String> submitResult(@Body String result);
}
