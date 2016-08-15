package moe.gcd.jce.exception;

import java.nio.ByteBuffer;

public interface IllegalArgumentExceptionHandler {
    void onException(IllegalArgumentException exception, ByteBuffer buffer, int argument1, int argument2);
}