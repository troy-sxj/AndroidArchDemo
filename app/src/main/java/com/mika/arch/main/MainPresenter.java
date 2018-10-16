package com.mika.arch.main;

import com.mika.arch.main.usecase.GetDayUseCase;
import com.mika.lib.mvp.base.MvpPresenter;
import com.mika.lib.net.repository.impl.ApiRepositoryImpl;

import retrofit2.Call;

/**
 * @Author: mika
 * @Time: 2018/10/15 上午11:27
 * @Description:
 */
public class MainPresenter extends MvpPresenter<MainView> {

    private Call<String> today;

    private GetDayUseCase getDayUseCase;

    public MainPresenter(MainView view) {
        super(view);
        getDayUseCase = new GetDayUseCase(new ApiRepositoryImpl());
    }

    public void request() {
        getDayUseCase.execute(new GetDayUseCase.GetDayCallback() {
            @Override
            public void onGetDayInfo(String content) {
                if (isViewAttach()) {
                    getView().onGetContent(content);
                }
            }
        });
    }

    @Override
    protected void release() {
        if (today != null) {
            today.cancel();
            today = null;
        }
    }
}
