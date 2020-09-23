package com.example.book_store.service;

import com.example.book_store.model.Book;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BookService {
    List<Book> findAll();

    List<Book> findAllByCategoryId(Long categoryId);

    Book findById(Long id);

    Book saveBook(String name, Integer stock, Long categoryId);

    Book saveBook(Book book, MultipartFile image) throws IOException;

    Book updateBook(Long id, Book book, MultipartFile image) throws IOException;

    void deleteById(Long id);
}
