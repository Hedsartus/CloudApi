package ru.netology.cloudapi.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorData extends RuntimeException {
    private HttpStatus httpStatus;

    public ErrorData(String msg, HttpStatus httpStatus) {
        super(msg);
        this.httpStatus = httpStatus;
    }
}
