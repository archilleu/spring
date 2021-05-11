package com.lr.ioc.exception;

public class IocRuntimeException extends RuntimeException {

    public IocRuntimeException() {
    }

    public IocRuntimeException(String message) {
        super(message);
    }

    public IocRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public IocRuntimeException(Throwable cause) {
        super(cause);
    }
}
