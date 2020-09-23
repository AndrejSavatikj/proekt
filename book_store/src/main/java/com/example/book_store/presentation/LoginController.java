package com.example.book_store.presentation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/login")
public class LoginController {
    private List<String> allowedNames;

    @PostConstruct
    public void init() {
        this.allowedNames = new ArrayList<>();
        this.allowedNames.add("testUser");
    }

    @GetMapping
    public String getLoginPage() {
        return "login";
    }
}
