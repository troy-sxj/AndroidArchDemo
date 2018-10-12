package com.mika.lib.net.repository;

import com.mika.lib.net.data.entity.BaseBean;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiRepository extends DataRepository{

    @GET("today")
    Call<String> getToday();
}
