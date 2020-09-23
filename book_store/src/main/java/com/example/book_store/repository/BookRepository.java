package com.example.book_store.repository;

import com.example.book_store.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByCategoryId(Long categoryId);
    List<Book> findAllByOrderByPriceAsc();
    List<Book> findAllByOrderByPriceDesc();

    Optional<Book> findById(Long id);

    Book save(Book book);

    void deleteById(Long id);
}
