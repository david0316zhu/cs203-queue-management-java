package com.ticketmasterdemo.demo.common.exception;

public class InvalidArgsException extends RuntimeException {
    public InvalidArgsException(String message){
        super(message);
    }
    public InvalidArgsException(String message, Throwable e){
        super(message, e);
    }
}
