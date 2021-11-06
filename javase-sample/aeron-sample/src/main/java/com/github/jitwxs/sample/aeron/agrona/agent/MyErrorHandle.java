package com.github.jitwxs.sample.aeron.agrona.agent;

import org.agrona.ErrorHandler;

public class MyErrorHandle implements ErrorHandler {
    @Override
    public void onError(Throwable throwable) {
        System.out.println("MyErrorHandle catch error: " + throwable.getLocalizedMessage());
    }
}