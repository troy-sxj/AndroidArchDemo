package com.mika.lib.net.repository.impl;

import com.mika.lib.net.RestApi;
import com.mika.lib.net.repository.ApiRepository;
import com.mika.lib.net.service.ApiService;

import retrofit2.Call;

public class ApiRepositoryImpl extends DataRepositoryImpl implements ApiRepository {

    @Override
    public Call<String> getToday() {
        return createService(ApiService.class).getToday();
    }

    @Override
    protected String getRestUrl() {
        return RestApi.Url.GankIOAPi;
    }
}
