package com.practice.bsty.rxjava;

/**
 * Created by bsty on 21/03/2018.
 * <p>
 * 通过大量的抽象工厂模式
 * <p>
 * 每调用一次操作符的方法，就相当于在上层数据源和下层观察者之间桥接了一个新的Observable。
 * 桥接的Observable内部会实例化有新的OnSuscribe和Subscriber。OnSuscribe负责接受目标Subscriber传来的订阅请求，
 * 并调用源Observable.OnSubscribe的subscribe方法。源Observable.OnSubscribe将Event往下发送给桥接
 * Observable.Subscriber，最终桥接Observable.Subscriber将Event做相应处理后转发给目标Subscriber。
 */

public class Observable<T> {
    final OnSubscriber<T> onSubscriber;

    private Observable(OnSubscriber<T> onSubscriber) {
        this.onSubscriber = onSubscriber;
    }

    /**
     * RxJava创建入口，创建一个观察者，该观察者包含一个我们需要在订阅者接收到消息时进行处理的回调方法的接口。
     *
     * @param onSubscriber
     * @param <T>
     * @return
     */
    public static <T> Observable<T> create(OnSubscriber<T> onSubscriber) {
        return new Observable<>(onSubscriber);
    }

    /**
     * 订阅方法的处理逻辑
     *
     * @param subscriber
     */
    public void subscribe(Subscriber<? super T> subscriber) {
        //订阅者开始接收消息
        subscriber.onStart();
        //接口回调处理，我们调用该函数的时候需要实现相应的逻辑，
        onSubscriber.call(subscriber);
    }

    /**
     * 自定义实现转换的操作符
     * <p>
     * 最终返回的结果是Observable<R>,关键思路是
     * 1.调用create方法，参数是OnSubscriber<R>接口， OnSubscriber中subscribe()会回调的call方法中
     * 在该方法内进行subscribe(<T>泛型的Subscriber)的处理即可
     * 2. 在OnSubscriber重写的onNext(T t)中调用transform接口的回调方法
     * 将T转换成R既可。
     *
     * @param transformer
     * @param <R>
     * @return
     */
    public <R> Observable<R> map(final Transformer<? super T, ? extends R> transformer) {
        //操作符的实现原理

//        return create(new OnSubscriber<R>() {
//            @Override
//            public void call(final Subscriber<? super R> subscriber) {
//                Observable.this.subscribe(new Subscriber<T>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable throwable) {
//
//                    }
//
//                    @Override
//                    public void onNext(T t) {
//                        subscriber.onNext(transformer.call(t));
//                    }
//                });
//            }
//        });
        //使用重构的方式，减少代码臃肿
        return create(new MapOnSubscribe<>(this, transformer));
    }

    public Observable<T> subscribeOn(final Scheduler scheduler) {
        return Observable.create(new OnSubscriber<T>() {
            @Override
            public void call(final Subscriber<? super T> subscriber) {
                subscriber.onStart();
                scheduler.createWorker().schedule(new Runnable() {
                    @Override
                    public void run() {
                        Observable.this.onSubscriber.call(subscriber);
                    }
                });
            }
        });
    }

    public Observable<T> observeOn(final Scheduler scheduler) {
        return Observable.create(new OnSubscriber<T>() {
            @Override
            public void call(final Subscriber<? super T> subscriber) {
                subscriber.onStart();
                final Scheduler.Worker worker = scheduler.createWorker();
                Observable.this.onSubscriber.call(new Subscriber<T>() {
                    @Override
                    public void onCompleted() {
                        worker.schedule(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        worker.schedule(new Runnable() {
                            @Override
                            public void run() {

                            }
                        });
                    }

                    @Override
                    public void onNext(final T t) {
                        worker.schedule(new Runnable() {
                            @Override
                            public void run() {
                                subscriber.onNext(t);
                            }
                        });
                    }
                });
            }
        });
    }

    public interface OnSubscriber<T> {
        void call(Subscriber<? super T> subscriber);
    }

    public interface Transformer<T, R> {
        R call(T t);
    }
}
