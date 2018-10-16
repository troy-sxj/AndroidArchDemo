package com.mika.lib.net.service;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface ApiService {

    @GET("today")
    Observable<String> getToday();
}
