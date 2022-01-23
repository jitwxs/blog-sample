package com.github.jitwxs.sample.grpc.pcbook;

public class AlreadyExistsException extends RuntimeException {
    public AlreadyExistsException(String message) {
        super(message);
    }
}
