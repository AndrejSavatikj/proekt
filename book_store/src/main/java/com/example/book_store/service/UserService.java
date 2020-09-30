package com.example.book_store.service;

import com.example.book_store.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    List<User> findAll();
    User findById(String userId);
    User registerUser(User user);
    void deleteUser(String username);
    User editUser(String username, String newUsername);
}
