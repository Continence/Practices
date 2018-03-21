package com.practice.bsty.rxjava;

/**
 * Created by bsty on 21/03/2018.
 * 重构map操作符
 */

public class MapOnSubscribe<T, R> implements Observable.OnSubscriber<R> {
    final Observable<T> source;
    final Observable.Transformer<? super T, ? extends R> transformer;

    public MapOnSubscribe(Observable<T> source, Observable.Transformer<? super T, ? extends R> transformer) {
        this.source = source;
        this.transformer = transformer;
    }

    @Override
    public void call(Subscriber<? super R> subscriber) {
        source.subscribe(new MapSubscribe<>(subscriber,transformer));
    }

    public class MapSubscribe<T, R> extends Subscriber<R> {

        final Subscriber<? super T> actual;
        final Observable.Transformer<? super R, ? extends T> transformer;

        public MapSubscribe(Subscriber<? super T> actual, Observable.Transformer<? super R, ? extends T> transformer) {
            this.actual = actual;
            this.transformer = transformer;
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable throwable) {

        }

        @Override
        public void onNext(R r) {
            actual.onNext(transformer.call(r));
        }
    }
}
