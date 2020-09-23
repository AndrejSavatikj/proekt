package com.example.book_store.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BookIsAlreadyInWishListException extends RuntimeException {
    public BookIsAlreadyInWishListException(String bookName) {
        super(String.format("%s is already in the wish list", bookName));
    }
}
