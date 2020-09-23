package com.example.book_store.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ShoppingCartIsNotActiveException extends RuntimeException{
    public ShoppingCartIsNotActiveException(String userId) {
        super(String.format("User %s has no active cart", userId));
    }
}
