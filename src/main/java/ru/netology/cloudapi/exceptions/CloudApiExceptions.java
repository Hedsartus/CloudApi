package ru.netology.cloudapi.exceptions;

import org.springframework.http.HttpStatus;

public interface CloudApiExceptions {
    HttpStatus getStatus();
    String getMessage();
}
