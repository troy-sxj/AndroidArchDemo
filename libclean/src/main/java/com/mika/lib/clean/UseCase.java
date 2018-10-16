package com.mika.lib.clean;

import android.support.annotation.NonNull;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @Author: mika
 * @Time: 2018/10/15 下午3:10
 * @Description:
 */
public abstract class UseCase<D extends DataRepository, R extends BaseReaParameter, C extends BaseCallback, T> implements BaseUseCase<R, C, T> {

    private CompositeDisposable compositeDisposable;
    protected D dataRepository;

    public UseCase(@NonNull D dataRepository) {
        this.dataRepository = dataRepository;
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public ObservableTransformer<T, T> buildTransformer() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    @Override
    public void execute(R req, C callback) {
        execute(buildTransformer(), req, callback);
    }

    public void execute(C callback) {
        execute(null, callback);
    }

    public void execute(ObservableTransformer<T, T> transformer, R req, C callback) {
        ConsumerSet<T> consumerSet = buildSubscriber(req, callback);
        Consumer<T> onNextConsumer = consumerSet.onNextConsumer;
        Consumer<Throwable> onErrorConsumer = consumerSet.onErrorConsumer;
        Action onCompleteConsumer = consumerSet.onCompleteConsumer;
        Disposable disposable = buildObservable(req, callback)
                .compose(transformer)
                .doOnComplete(onCompleteConsumer)
                .doOnError(onErrorConsumer)
                .subscribe(onNextConsumer, onErrorConsumer, onCompleteConsumer);
        compositeDisposable.add(disposable);
    }

    @Override
    public void release() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
            compositeDisposable = null;
        }
    }
}
