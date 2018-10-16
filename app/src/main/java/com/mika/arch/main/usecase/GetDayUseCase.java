package com.mika.arch.main.usecase;

import android.support.annotation.NonNull;

import com.mika.lib.clean.BaseCallback;
import com.mika.lib.clean.BaseReaParameter;
import com.mika.lib.clean.ConsumerSet;
import com.mika.lib.clean.UseCase;
import com.mika.lib.net.repository.ApiRepository;

import io.reactivex.Observable;


/**
 * @Author: mika
 * @Time: 2018/10/15 下午4:04
 * @Description:
 */
public class GetDayUseCase extends UseCase<ApiRepository, BaseReaParameter, GetDayUseCase.GetDayCallback, String> {

    public GetDayUseCase(@NonNull ApiRepository dataRepository) {
        super(dataRepository);
    }

    @Override
    public Observable<String> buildObservable(BaseReaParameter req, GetDayUseCase.GetDayCallback callback) {
        return dataRepository.getToday();
    }

    @Override
    public ConsumerSet<String> buildSubscriber(BaseReaParameter req, final GetDayUseCase.GetDayCallback callback) {
        return new ConsumerSet<String>() {
            @Override
            protected void onSafeNext(String var) {
                if(callback != null){
                    callback.onGetDayInfo(var);
                }
            }

            @Override
            protected void onSafeError(Throwable t) {
                if(callback !=null ){
                    callback.onGetDayInfo(t.getMessage());
                }
            }

            @Override
            protected void onSafeComplete() {

            }
        };
    }

    public interface GetDayCallback extends BaseCallback{

        void onGetDayInfo(String content);
    }
}
