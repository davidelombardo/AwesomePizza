package com.example.AwesomePizza.exception;

public class NoSuchOrderException extends RuntimeException {
    public NoSuchOrderException(String message){
        super(message);
    }
}
