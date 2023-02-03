package org.template.test.assignment.communication.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface XmlApi {

  @GET("/")
  Call<String> getXml();
}
