package com.practice.bsty.rxjava;

import java.util.concurrent.Executors;

/**
 * Created by bsty on 24/03/2018.
 */

public class Schedulers {
    private static final Scheduler ioScheduler = new Scheduler(Executors.newSingleThreadExecutor());
    public static Scheduler io(){
        return ioScheduler;
    }
}
