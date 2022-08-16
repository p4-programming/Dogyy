package com.bnb.doggydoo.homemodule.ui;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

//public class MyApi {
//}
public interface MyApi {
    @GET("maps/api/directions/json")
    Call<DirectionResults> getJson(@Query("origin") String origin, @Query("destination") String destination);
}