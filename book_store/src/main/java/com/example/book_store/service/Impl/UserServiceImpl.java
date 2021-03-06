package com.example.book_store.service.Impl;

import com.example.book_store.model.User;
import com.example.book_store.model.exceptions.UserNotFoundException;
import com.example.book_store.model.exceptions.UsernameAlreadyExistsException;
import com.example.book_store.repository.UserRepository;
import com.example.book_store.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository ur;

    public UserServiceImpl(UserRepository ur) {
        this.ur = ur;
    }

    @Override
    public List<User> findAll() {
        return this.ur.findAll();
    }

    public User findById(String userId) {
        return this.ur.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Override
    public User registerUser(User user) {
        if (this.ur.existsById(user.getUsername())){
            throw new UsernameAlreadyExistsException(user.getUsername());
        }
        return this.ur.save(user);
    }

    @Override
    public void deleteUser(String username) {
        this.ur.delete(this.ur.findById(username).orElseThrow(() -> new UserNotFoundException(username)));
    }

    @Override
    public User expire(String userId) {
        User user = this.findById(userId);
        user.setAccountNonExpired(!user.isAccountNonExpired());
        return this.ur.save(user);
    }

    @Override
    public User lock(String userId) {
        User user = this.findById(userId);
        user.setAccountNonLocked(!user.isAccountNonLocked());
        return this.ur.save(user);
    }

    @Override
    public User credentialExpire(String userId) {
        User user = this.findById(userId);
        user.setCredentialsNonExpired(!user.isCredentialsNonExpired());
        return this.ur.save(user);
    }

    @Override
    public User enable(String userId) {
        User user = this.findById(userId);
        user.setEnabled(!user.isEnabled());
        return this.ur.save(user);
    }


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return this.ur.findById(s).orElseThrow(() -> new UsernameNotFoundException(s));
    }


}
