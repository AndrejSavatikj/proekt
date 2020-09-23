package com.example.book_store.service.Impl;

import com.example.book_store.model.Book;
import com.example.book_store.model.User;
import com.example.book_store.model.WishList;
import com.example.book_store.model.exceptions.BookIsAlreadyInWishListException;
import com.example.book_store.model.exceptions.WishListIsNotActiveException;
import com.example.book_store.repository.WishListRepository;
import com.example.book_store.service.BookService;
import com.example.book_store.service.UserService;
import com.example.book_store.service.WishListService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishListServiceImpl implements WishListService {
    private final UserService userService;
    private final BookService bookService;
    private final WishListRepository wishListRepository;

    public WishListServiceImpl(UserService userService, BookService bookService, WishListRepository wishListRepository) {
        this.userService = userService;
        this.bookService = bookService;
        this.wishListRepository = wishListRepository;
    }

    @Override
    public WishList findActiveWishListByUsername(String userId) {
        return this.wishListRepository.findByUserUsername(userId).orElseThrow(() -> new WishListIsNotActiveException(userId));
    }

    @Override
    public List<WishList> findAllByUsername(String userId) {
        return this.wishListRepository.findAllByUserUsername(userId);
    }

    @Override
    public WishList createWishList(String userId) {
        User user = this.userService.findById(userId);
        WishList wishList = new WishList();
        wishList.setUser(user);
        return this.wishListRepository.save(wishList);
    }

    @Override
    public WishList addBookToWishList(String userId, Long bookId) {
        WishList wishList = this.getActiveWishList(userId);
        Book book = this.bookService.findById(bookId);
        for (Book book1 : wishList.getBooks()){
            if (book1.getId().equals(bookId)) {
                throw new BookIsAlreadyInWishListException(book1.getName());
            }
        }
        wishList.getBooks().add(book);
        return this.wishListRepository.save(wishList);
    }

    @Override
    public WishList removeBookFromWishList(String userId, Long bookId) {
        WishList wishList = this.getActiveWishList(userId);
        wishList.setBooks(
                wishList.getBooks()
                .stream()
                .filter(book -> !book.getId().equals(bookId))
                .collect(Collectors.toList())
        );
        return this.wishListRepository.save(wishList);
    }

    @Override
    public WishList getActiveWishList(String userId) {
        return this.wishListRepository.findByUserUsername(userId).orElseGet(() -> {
            WishList wishList = new WishList();
            User user = this.userService.findById(userId);
            wishList.setUser(user);
            return this.wishListRepository.save(wishList);
        });
    }
}
