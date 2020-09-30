package com.example.book_store.service.Impl;

import com.example.book_store.model.Author;
import com.example.book_store.model.exceptions.AuthorNotFoundException;
import com.example.book_store.repository.AuthorRepository;
import com.example.book_store.service.AuthorService;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository ar;

    public AuthorServiceImpl(AuthorRepository ar) {
        this.ar = ar;
    }

    @Override
    public List<Author> findAll() {
        return this.ar.findAll();
    }

    @Override
    public List<Author> findAllByName(String name) {
        return this.ar.findAllByName(name);
    }

    @Override
    public Author findById(Long id) {
        return this.ar.findById(id).orElseThrow(() -> new AuthorNotFoundException(id));
    }

    @Override
    public Author save(Author author) {
        return this.ar.save(author);
    }

    @Override
    public Author updateName(Long id, String name) {
        Author author = this.findById(id);
        author.setName(name);
        return this.ar.save(author);
    }

    @Override
    public void deleteById(Long id) {
        this.ar.deleteById(id);
    }

    @PostConstruct
    public void init(){
        Author author = new Author("author1");
        this.ar.save(author);
    }
}
