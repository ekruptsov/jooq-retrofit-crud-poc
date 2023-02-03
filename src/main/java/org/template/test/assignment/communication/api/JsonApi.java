package org.template.test.assignment.communication.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface JsonApi {

  @POST("/status")
  Call<String> submitResult(@Body String result);
}
