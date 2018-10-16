package com.mika.lib.clean;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;

/**
 * @Author: mika
 * @Time: 2018/10/15 下午3:11
 * @Description:
 */
public interface BaseUseCase<R extends BaseReaParameter, C extends BaseCallback, T> {

    Observable<T> buildObservable(R req, C callback);


    ConsumerSet<T> buildSubscriber(R req, C callback);

    ObservableTransformer<T, T> buildTransformer();

    void execute(R req, C callback);

    void release();
}
