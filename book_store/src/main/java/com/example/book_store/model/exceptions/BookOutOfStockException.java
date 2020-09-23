package com.example.book_store.model.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BookOutOfStockException extends RuntimeException {
    public BookOutOfStockException(String bookName) {
        super(String.format("%s is out of stock, sorry!", bookName));
    }
}
