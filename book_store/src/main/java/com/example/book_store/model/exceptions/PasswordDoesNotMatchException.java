package com.example.book_store.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class PasswordDoesNotMatchException extends RuntimeException{
    public PasswordDoesNotMatchException() {
        super(String.format("Passwords don't match"));
    }
}
