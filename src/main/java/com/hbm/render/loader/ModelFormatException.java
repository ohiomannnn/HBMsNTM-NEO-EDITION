package com.hbm.render.loader;

public class ModelFormatException extends RuntimeException {
    public ModelFormatException(String message) {
        super(message);
    }

    public ModelFormatException(String message, Throwable cause) {
        super(message, cause);
    }
}
