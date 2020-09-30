package com.example.book_store.presentation;

import com.example.book_store.model.Role;
import com.example.book_store.model.User;
import com.example.book_store.model.enumerations.CartStatus;
import com.example.book_store.repository.RoleRepository;
import com.example.book_store.service.ShoppingCartService;
import com.example.book_store.service.UserService;
import com.example.book_store.service.WishListService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final ShoppingCartService shoppingCartService;
    private final WishListService wishListService;

    public AdminController(UserService userService, RoleRepository roleRepository, ShoppingCartService shoppingCartService, WishListService wishListService) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.shoppingCartService = shoppingCartService;
        this.wishListService = wishListService;
    }

    @GetMapping
    public String loadAdminPage(Model model) {
        List<User> users = this.userService.findAll().stream().filter(user -> !user.getUsername().equals("admin")).collect(Collectors.toList());
        model.addAttribute("users", users);
        return "admin";
    }

    @GetMapping("{username}/edit")
    public String editUser(Model model, @PathVariable String username) {
        try {
            User user = this.userService.findById(username);
            List<Role> roles = this.roleRepository.findAll();

            model.addAttribute("user", user);
            model.addAttribute("roles", roles);

            return "edit-user";
        } catch (RuntimeException ex) {
            return "redirect:/admin?error=" + ex.getMessage();
        }
    }

    @PostMapping("/edit-user")
    @Secured("ROLE_ADMIN")
    public String updateUser(User user, String newUsername) {
        System.out.println(user.getUsername());
        System.out.println(newUsername);
        this.userService.editUser(user.getUsername(), newUsername);
        System.out.println(user.getUsername());
        return "redirect:/admin";
    }

    @PostMapping("/{username}/deleteUser")
    @Secured("ROLE_ADMIN")
    public String deleteUser(@PathVariable String username) {
        this.wishListService.deleteWishList(username);

        if (this.shoppingCartService.existsByUserUsernameAndStatus(username, CartStatus.CREATED)) {
            this.shoppingCartService.cancelActiveShoppingCart(username);
        }

        this.shoppingCartService.deleteShoppingCartsById(username);
        this.userService.deleteUser(username);
        return "redirect:/admin";
    }


}
