package com.example.book_store.service.Impl;

import com.example.book_store.model.Role;
import com.example.book_store.model.User;
import com.example.book_store.model.exceptions.PasswordDoesNotMatchException;
import com.example.book_store.model.exceptions.UserIsAlreadyModeratorException;
import com.example.book_store.repository.RoleRepository;
import com.example.book_store.repository.UserRepository;
import com.example.book_store.service.AuthService;
import com.example.book_store.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserService userService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public String getCurrentUserId() {
        return this.getCurrentUser().getUsername();
    }

    @Override
    public User signUpUser(String username, String password, String repeatedPassword) {
        if (!password.equals(repeatedPassword)) {
            throw new PasswordDoesNotMatchException();
        }

        User user = new User(username, this.passwordEncoder.encode(password));
        Role userRole = this.roleRepository.findByName("ROLE_USER");
        user.setRoles(Collections.singletonList(userRole));
        return this.userService.registerUser(user);

    }

    @Override
    public User makeUserAdmin(String userId) {
        User user = this.userService.findById(userId);
        List<Role> userRoles = user.getRoles();
        if (!userRoles.contains(this.roleRepository.findByName("ROLE_ADMIN"))) {
            userRoles.add(this.roleRepository.findByName("ROLE_ADMIN"));
            user.setRoles(userRoles);
            return this.userRepository.save(user);
        }
        else throw new UserIsAlreadyModeratorException(userId);
    }

    @Override
    public User removeUserAdmin(String userId) {
        User user = this.userService.findById(userId);
        List<Role> userRoles = user.getRoles();
        if (userRoles.contains(this.roleRepository.findByName("ROLE_ADMIN"))) {
            userRoles.remove(this.roleRepository.findByName("ROLE_ADMIN"));
            user.setRoles(userRoles);
            return this.userRepository.save(user);
        }
        else throw new RuntimeException("User is not a moderator");
    }

    @PostConstruct
    public void init() {
        if (!this.userRepository.existsById("admin")) {
            Role role = new Role("ROLE_ADMIN");
            Role role2 = new Role("ROLE_USER");
            this.roleRepository.save(role);
            this.roleRepository.save(role2);
            User admin = new User("admin", this.passwordEncoder.encode("admin"));
            admin.setRoles(this.roleRepository.findAll());
            this.userRepository.save(admin);
        }
    }
}
