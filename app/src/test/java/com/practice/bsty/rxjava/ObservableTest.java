package com.practice.bsty.rxjava;

import org.junit.Test;

/**
 * Created by bsty on 21/03/2018.
 */
public class ObservableTest {
    @Test
    public void create() throws Exception {
        Observable.create(new Observable.OnSubscriber<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 10; i++) {
                    subscriber.onNext(i);
                }
            }
        }).subscribe(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                System.out.println("onCompleted");
            }

            @Override
            public void onError(Throwable throwable) {
                System.err.println("throwable:" + throwable.getMessage());
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println(integer);
            }
        });
    }

    @Test
    public void subscribe() throws Exception {
    }

    @Test
    public void map() throws Exception {
        Observable.create(new Observable.OnSubscriber<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 0; i < 10; i++) {
                    subscriber.onNext(i);
                }
            }
        }).map(new Observable.Transformer<Integer, String>() {
            @Override
            public String call(Integer integer) {
                return "mapping "+integer;
            }
        }).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onNext(String s) {
                System.out.println("S:" + s);
            }
        });
    }
}