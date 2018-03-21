package com.practice.bsty.rxjava;

/**
 * Created by bsty on 21/03/2018.
 */

public interface Observer<T> {
    void onCompleted();

    void onError(Throwable throwable);

    void onNext(T t);
}
