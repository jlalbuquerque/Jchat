package com.jlalbuquerq.display.internal.concurrent;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class MainServerThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable r) {
        ThreadFactory factory = Executors.defaultThreadFactory();

        Thread thread = factory.newThread(r);
        thread.setUncaughtExceptionHandler(new MainServerThreadFactoryExceptionHandler());

        return thread;
    }
}
