package com.example.cms.exceptions;

public class EmptyPageException extends RuntimeException {

    public EmptyPageException(String message) {
        super(message);
    }
}