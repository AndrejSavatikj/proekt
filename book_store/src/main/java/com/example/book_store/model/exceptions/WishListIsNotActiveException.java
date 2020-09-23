package com.example.book_store.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class WishListIsNotActiveException extends RuntimeException{
    public WishListIsNotActiveException(String userId) {
        super(String.format("User %s has no active wish list", userId));
    }
}
