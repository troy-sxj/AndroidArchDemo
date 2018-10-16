package com.mika.lib.clean;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * @Author: mika
 * @Time: 2018/10/15 下午3:39
 * @Description:
 */
public abstract class ConsumerSet<T> {

    public ConsumerSet() {
    }

    Consumer<T> onNextConsumer = new Consumer<T>() {
        @Override
        public void accept(T t) {
            try {
                ConsumerSet.this.onSafeNext(t);
            } catch (Exception e) {
                ConsumerSet.this.onSafeError(e);
            }
        }
    };

    Consumer<Throwable> onErrorConsumer = new Consumer<Throwable>() {
        @Override
        public void accept(Throwable o) {
            ConsumerSet.this.onSafeError(o);
        }
    };

    Action onCompleteConsumer = new Action() {
        @Override
        public void run() {
            ConsumerSet.this.onSafeComplete();
        }
    };

    public Consumer getOnNextConsumer() {
        return onNextConsumer;
    }

    public Consumer getOnErrorConsumer() {
        return onErrorConsumer;
    }

    public Action getOnCompleteConsumer() {
        return onCompleteConsumer;
    }

    protected abstract void onSafeNext(T var);

    protected abstract void onSafeError(Throwable t);

    protected abstract void onSafeComplete();


}
