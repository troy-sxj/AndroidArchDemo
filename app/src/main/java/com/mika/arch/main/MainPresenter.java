package com.mika.arch.main;

import com.mika.lib.mvp.base.MvpPresenter;
import com.mika.lib.net.repository.impl.ApiRepositoryImpl;
import com.mika.lib.util.android.ArchLog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @Author: mika
 * @Time: 2018/10/15 上午11:27
 * @Description:
 */
public class MainPresenter extends MvpPresenter<MainView> {

    private Call<String> today;

    public MainPresenter(MainView view) {
        super(view);
    }

    public void request() {
        ApiRepositoryImpl apiTodayRepository = new ApiRepositoryImpl();
        today = apiTodayRepository.getToday();
        today.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (isViewAttach()) {
                    getView().onGetContent(response.body().getBytes().toString());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if(isViewAttach()){
                    getView().onGetContent(t.getMessage());
                }
            }
        });
    }

    @Override
    protected void release() {
        if(today != null){
            today.cancel();
            today = null;
        }
    }
}
