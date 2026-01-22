package com.ctoutweb.argentDePoche.core.domain.exception;

public class ChildImageException  extends  RuntimeException {
    public ChildImageException(String message) {
        super(message);
    }

    public ChildImageException(String message, Throwable cause) {
        super(message, cause);
    }
}
