package com.github.jitwxs.sample.aeron.agent;

import org.agrona.ErrorHandler;

class MyErrorHandle implements ErrorHandler {
    @Override
    public void onError(Throwable throwable) {
        System.out.println("MyErrorHandle catch error: " + throwable.getLocalizedMessage());
    }
}