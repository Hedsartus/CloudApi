package ru.netology.cloudapi.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.netology.cloudapi.exceptions.ErrorData;

@ControllerAdvice
public class DefaultAdvice {
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> unauthorizedUserHandler(BadCredentialsException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<String> usernameNotFoundExceptionHandler(UsernameNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ErrorData.class)
    public ResponseEntity<String> usernameNotFoundExceptionHandler(ErrorData e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

}
