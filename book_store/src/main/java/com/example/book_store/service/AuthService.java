package com.example.book_store.service;

import com.example.book_store.model.User;

public interface AuthService {
    User getCurrentUser();
    String getCurrentUserId();
    User signUpUser(String username, String password, String repeatedPassword);
    User makeUserAdmin(String userId);
    User removeUserAdmin(String userId);
}
