package com.ibs21.OnlineShop.controllers;

import com.ibs21.OnlineShop.domain.Product;
import com.ibs21.OnlineShop.domain.Role;
import com.ibs21.OnlineShop.domain.User;
import com.ibs21.OnlineShop.repos.UserRepository;
import com.ibs21.OnlineShop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")

public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping()
    public String userList(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users/user-list";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "users/user-edit";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping
    public String userSaveEditForm(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user
    ) {

       userService.saveUser(user, username, form);
        return "redirect:/users";
    }

    @GetMapping("profile")
        public String getProfileInfo(Model model, @AuthenticationPrincipal User user){
            Set<Product> products = user.getProducts();
            model.addAttribute("products", products);

            return "profile";
        }

    @GetMapping("profile/user-modify")
    public String getProfile(Model model, @AuthenticationPrincipal User user){

        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());

        return "users/user-modify";
    }



    @PostMapping("profile/user-modify")
    public String updateProfile(
            @AuthenticationPrincipal User user,
            @RequestParam String password,
            @RequestParam String email
    ){
        userService.updateProfile(user, passwordEncoder.encode(password), email);
        return "redirect:/users/profile";
    }


//    @GetMapping("profile")
//    public String userProducts(@AuthenticationPrincipal User currentUser,
//                               @PathVariable User user,
//                               Model model
//                               ){
//
//        Set<Product> products = user.getProducts();
//
//        model.addAttribute("products", products);
//
//
//        return "users/user-edit";
//    }
}
