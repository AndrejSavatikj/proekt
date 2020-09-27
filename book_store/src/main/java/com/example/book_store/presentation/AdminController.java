package com.example.book_store.presentation;

import com.example.book_store.model.User;
import com.example.book_store.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String loadAdminPage(Model model) {
        List<User> users = this.userService.findAll();
        model.addAttribute("users", users);
        return "admin";
    }


}
