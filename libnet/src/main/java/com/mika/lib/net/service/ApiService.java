package com.mika.lib.net.service;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("today")
    Call<String> getToday();
}
