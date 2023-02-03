package org.poc.jooq_retrofit.communication.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface XmlApi {

  @GET("/xml")
  Call<String> getXml();
}
