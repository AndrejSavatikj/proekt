package com.example.book_store.service.Impl;

import com.example.book_store.model.Book;
import com.example.book_store.model.Category;
import com.example.book_store.model.exceptions.BookNotFoundException;
import com.example.book_store.repository.BookRepository;
import com.example.book_store.service.AuthorService;
import com.example.book_store.service.BookService;
import com.example.book_store.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository br;
    private final CategoryService cs;
    private final AuthorService as;

    public BookServiceImpl(BookRepository br, CategoryService cs, AuthorService as) {
        this.br = br;
        this.cs = cs;
        this.as = as;
    }

    @Override
    public List<Book> findAll() {
        return this.br.findAll();
    }

    @Override
    public List<Book> findAllByCategoryId(Long categoryId) {
        return this.br.findAllByCategoryId(categoryId);
    }

    @Override
    public Book findById(Long id) {
        return this.br.findById(id).orElseThrow(() -> new BookNotFoundException(id));
    }

    @Override
    public Book saveBook(String name, Integer stock, Long categoryId) {
        return null;
    }

    @Override
    public Book saveBook(Book book, MultipartFile image) throws IOException {
//        Category category = this.cs.findById(book.getCategory().getId());
//        book.setCategory(category);

        return getFinalBookWithImage(book, image);
    }

    private Book getFinalBookWithImage(Book book, MultipartFile image) throws IOException {
        if (image != null && !image.getName().isEmpty()) {
            byte[] bytes = image.getBytes();
            String base64Image = String.format("data:%s;base64,%s", image.getContentType(), Base64.getEncoder().encodeToString(bytes));
            book.setImage64Base(base64Image);
        }
        return this.br.save(book);
    }

    @Override
    public Book updateBook(Long id, Book book, MultipartFile image) throws IOException {
        Book b = this.findById(id);
        Category category = this.cs.findById(book.getCategory().getId());
        b.setCategory(category);
        b.setQuantity(book.getQuantity());
        b.setPrice(book.getPrice());
        b.setName(book.getName());
        return getFinalBookWithImage(b, image);
    }

    @Override
    public void deleteById(Long id) {
        this.br.deleteById(id);
    }
}
