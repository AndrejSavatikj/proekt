package com.example.book_store.presentation;

import com.example.book_store.model.Author;
import com.example.book_store.model.Book;
import com.example.book_store.model.Category;
import com.example.book_store.service.AuthorService;
import com.example.book_store.service.BookService;
import com.example.book_store.service.CategoryService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {
    private final BookService bs;
    private final CategoryService cs;
    private final AuthorService as;

    public BookController(BookService bs, CategoryService cs, AuthorService as) {
        this.bs = bs;
        this.cs = cs;
        this.as = as;
    }

    @GetMapping
    public String getBookPage(Model model) {
        List<Book> books = this.bs.findAll();
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/new-book")
    @Secured("ROLE_ADMIN")
    public String addNewBookPage(Model model) {
        List<Category> categories = this.cs.findAll();
        List<Author> authors = this.as.findAll();

        model.addAttribute("categories", categories);
        model.addAttribute("book", new Book());
        model.addAttribute("authors", authors);

        return "add-book";
    }

    @GetMapping("/{id}/edit")
    public String editBooksPage(Model model, @PathVariable Long id) {
        try {
            Book book = this.bs.findById(id);
            List<Category> categories = this.cs.findAll();
            List<Author> authors = this.as.findAll();

            model.addAttribute("book", book);
            model.addAttribute("authors", authors);
            model.addAttribute("categories", categories);

            return "add-book";
        } catch (RuntimeException ex) {
            return "redirect:/books?error=" + ex.getMessage();
        }
    }

    @PostMapping
    public String saveBook(
            @Valid Book book,
            BindingResult bindingResult,
            @RequestParam MultipartFile image,
            Model model) {
        if (bindingResult.hasErrors()) {
            List<Category> categories = this.cs.findAll();
            List<Author> authors = this.as.findAll();
            model.addAttribute("categories", categories);
            model.addAttribute("authors", authors);
            return "add-book";
        }
        try {
            this.bs.saveBook(book, image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "redirect:/books";
    }

    @PostMapping("/{id}/delete")
    public String deleteBook(@PathVariable Long id) {
        this.bs.deleteById(id);
        return "redirect:/books";
    }
}
