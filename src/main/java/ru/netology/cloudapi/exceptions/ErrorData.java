package ru.netology.cloudapi.exceptions;

import org.springframework.http.HttpStatus;

public class ErrorData extends RuntimeException implements CloudApiExceptions {
    private final HttpStatus httpStatus;

    public ErrorData(String msg, HttpStatus httpStatus) {
        super(msg);
        this.httpStatus = httpStatus;
    }

    @Override
    public HttpStatus getStatus() {
        return httpStatus;
    }
}
