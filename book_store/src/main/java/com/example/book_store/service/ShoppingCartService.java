package com.example.book_store.service;

import com.example.book_store.model.ShoppingCart;
import com.example.book_store.model.dto.ChargeRequest;
import com.example.book_store.model.enumerations.CartStatus;
import com.stripe.exception.StripeException;

import java.util.List;

public interface ShoppingCartService {
    ShoppingCart findActiveShoppingCartByUsername(String userId);

    List<ShoppingCart> findAllByUsername(String userId);

    ShoppingCart createNewShoppingCart(String userId);

    ShoppingCart addBookToShoppingCart(String userId,
                                          Long bookId);

    boolean existsByUserUsernameAndStatus(String username, CartStatus status);

    ShoppingCart removeBookFromShoppingCart(String userId, Long bookId);

    ShoppingCart getActiveShoppingCart(String userId);

    ShoppingCart cancelActiveShoppingCart(String userId);

    ShoppingCart checkoutShoppingCart(String userId, ChargeRequest chargeRequest) throws StripeException;

    void deleteShoppingCartsById(String userId);
}
