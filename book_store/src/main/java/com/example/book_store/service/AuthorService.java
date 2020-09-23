package com.example.book_store.service;

import com.example.book_store.model.Author;

import java.util.List;

public interface AuthorService {
    List<Author> findAll();
    List<Author> findAllByName(String name);
    Author findById(Long id);
    Author save(Author author);
    Author updateName(Long id, String name);
    void deleteById(Long id);
}
