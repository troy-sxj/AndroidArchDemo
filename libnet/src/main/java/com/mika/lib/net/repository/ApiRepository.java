package com.mika.lib.net.repository;

import com.mika.lib.clean.DataRepository;

import io.reactivex.Observable;
import retrofit2.http.GET;


public interface ApiRepository extends DataRepository {

    @GET("today")
    Observable<String> getToday();
}
