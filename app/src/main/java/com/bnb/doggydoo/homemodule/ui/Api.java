package com.bnb.doggydoo.homemodule.ui;

//public class Api {
//}

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {
    @GET("maps/api/directions/json")
    Call<DirectionResults> getJson(@Query("origin") String origin, @Query("destination") String destination);
}
