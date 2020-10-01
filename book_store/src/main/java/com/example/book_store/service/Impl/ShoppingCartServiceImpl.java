package com.example.book_store.service.Impl;

import com.example.book_store.model.Book;
import com.example.book_store.model.ShoppingCart;
import com.example.book_store.model.User;
import com.example.book_store.model.dto.ChargeRequest;
import com.example.book_store.model.enumerations.CartStatus;
import com.example.book_store.model.exceptions.*;
import com.example.book_store.repository.ShoppingCartRepository;
import com.example.book_store.service.BookService;
import com.example.book_store.service.PaymentService;
import com.example.book_store.service.ShoppingCartService;
import com.example.book_store.service.UserService;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.StripeException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    private final UserService userService;
    private final BookService bookService;
    private final PaymentService paymentService;
    private final ShoppingCartRepository shoppingCartRepository;

    public ShoppingCartServiceImpl(UserService userService, BookService bookService, PaymentService paymentService, ShoppingCartRepository shoppingCartRepository) {
        this.userService = userService;
        this.bookService = bookService;
        this.paymentService = paymentService;
        this.shoppingCartRepository = shoppingCartRepository;
    }

    @Override
    public ShoppingCart findActiveShoppingCartByUsername(String userId) {
        return this.shoppingCartRepository.findByUserUsernameAndStatus(userId, CartStatus.CREATED).orElseThrow(() -> new ShoppingCartIsNotActiveException(userId));
    }

    @Override
    public List<ShoppingCart> findAllByUsername(String userId) {
        return this.shoppingCartRepository.findAllByUserUsername(userId);
    }

    @Override
    public ShoppingCart createNewShoppingCart(String userId) {
        User user = this.userService.findById(userId);
        if (this.shoppingCartRepository.existsByUserUsernameAndStatus(
                user.getUsername(),
                CartStatus.CREATED
        )) {
            throw new ShoppingCartIsAlreadyCreatedException(userId);
        }
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        return this.shoppingCartRepository.save(shoppingCart);
    }
    @Transactional
    @Override
    public ShoppingCart addBookToShoppingCart(String userId, Long bookId) {
        ShoppingCart shoppingCart = this.getActiveShoppingCart(userId);
        Book book = this.bookService.findById(bookId);
        for (Book b : shoppingCart.getBooks()){
            if (b.getId().equals(bookId)) {
                throw new BookIsAlreadyInShoppingCartException(book.getName());
            }
        }
        if (book.getQuantity() <= 0){
            throw  new BookOutOfStockException(book.getName());
        }
        shoppingCart.getBooks().add(book);
        return this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public boolean existsByUserUsernameAndStatus(String username, CartStatus status) {
        return this.shoppingCartRepository.existsByUserUsernameAndStatus(username, status);
    }

    @Transactional
    @Override
    public ShoppingCart removeBookFromShoppingCart(String userId, Long bookId) {
        ShoppingCart shoppingCart = this.getActiveShoppingCart(userId);
        shoppingCart.setBooks(
                shoppingCart.getBooks()
                .stream() 
                .filter(book -> !book.getId().equals(bookId))
                .collect(Collectors.toList())
        );
        return this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart getActiveShoppingCart(String userId) {
        return this.shoppingCartRepository.findByUserUsernameAndStatus(userId, CartStatus.CREATED)
                .orElseGet(() -> {
                    ShoppingCart shoppingCart = new ShoppingCart();
                    User user = this.userService.findById(userId);
                    shoppingCart.setUser(user);
                    return this.shoppingCartRepository.save(shoppingCart);
                });
    }

    @Override
    public ShoppingCart cancelActiveShoppingCart(String userId) {
        ShoppingCart shoppingCart = this.shoppingCartRepository.findByUserUsernameAndStatus(userId, CartStatus.CREATED)
                .orElseThrow(() -> new ShoppingCartIsNotActiveException(userId));

        shoppingCart.setStatus(CartStatus.CANCELED);
        shoppingCart.setCloseDate(LocalDateTime.now());
        return this.shoppingCartRepository.save(shoppingCart);

    }
    @Transactional
    @Override
    public ShoppingCart checkoutShoppingCart(String userId, ChargeRequest chargeRequest) throws StripeException {
        ShoppingCart shoppingCart = this.shoppingCartRepository.findByUserUsernameAndStatus(userId, CartStatus.CREATED)
                .orElseThrow(() -> new ShoppingCartIsNotActiveException(userId));

        List<Book> books = shoppingCart.getBooks();

        for (Book book : books) {
            if (book.getQuantity() <= 0) {
                throw new BookOutOfStockException(book.getName());
            }
            book.setQuantity(book.getQuantity()-1);
        }
        try {
            this.paymentService.pay(chargeRequest);
        } catch (CardException | AuthenticationException | InvalidRequestException e) {
            throw new TransactionFailedException(userId, e.getMessage());
        }
        shoppingCart.setBooks(books);
        shoppingCart.setStatus(CartStatus.FINISHED);
        shoppingCart.setCloseDate(LocalDateTime.now());
        return this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public int countTransactionsByUsername(String username) {
        List<ShoppingCart> transactions = this.shoppingCartRepository.findAllByUserUsernameAndStatus(username, CartStatus.FINISHED);
        return transactions.size();
    }

    @Override
    public void deleteShoppingCartsById(String userId) {
        List<ShoppingCart> shoppingCartsToBeDeleted = this.findAllByUsername(userId);
        for (ShoppingCart cart : shoppingCartsToBeDeleted) {
            this.shoppingCartRepository.delete(cart);
        }
    }
}
