package com.example.cms.exceptions;

public class DuplicatePageException extends RuntimeException {

    public DuplicatePageException(String message) {
        super(message);
    }
}