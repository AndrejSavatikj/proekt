package com.example.book_store.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserIsAlreadyModeratorException extends RuntimeException{
    public UserIsAlreadyModeratorException(String userId) {
        super(String.format("%s is already a moderator", userId));
    }
}
