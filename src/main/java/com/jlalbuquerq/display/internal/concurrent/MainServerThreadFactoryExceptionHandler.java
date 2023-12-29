package com.jlalbuquerq.display.internal.concurrent;

import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainServerThreadFactoryExceptionHandler implements Thread.UncaughtExceptionHandler {
    private static final Logger LOGGER = Logger.getLogger("MainServerThreadFactoryExceptionHandlerLogger");

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (e instanceof SocketException) {
            LOGGER.log(Level.FINE, "Client suddenly disconnected");
        }
    }
}
