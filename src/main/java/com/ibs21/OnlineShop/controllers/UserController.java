package com.ibs21.OnlineShop.controllers;

import com.ibs21.OnlineShop.domain.Product;
import com.ibs21.OnlineShop.domain.Role;
import com.ibs21.OnlineShop.domain.User;
import com.ibs21.OnlineShop.repos.ProductRepository;
import com.ibs21.OnlineShop.repos.UserRepository;
import com.ibs21.OnlineShop.service.UserService;
import org.aspectj.weaver.ast.Var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Controller
@RequestMapping("/users")

public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Value("${upload.path}")
    private String uploadPath;


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
            var usr = userRepository.findById(user.getId()).get();
            Set<Product> products = usr.getProducts();
            model.addAttribute("username", user.getUsername());
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

    @GetMapping("/profile/{id}/edit")
    public String productEdit(@PathVariable(value = "id") Long id, Model model, Pageable pageable) {
        if (!productRepository.existsById(id)) {
            return "redirect:/home";
        }
        Page<Product> page;
        page = productRepository.findById(id, pageable);
        model.addAttribute("page", page);

        return "products/product-edit";
    }

    //    @PreAuthorize("hasRole('SELLER')")
    @PostMapping("/profile/{id}/edit")
    public String productUpdate(@PathVariable(value = "id") Long id, @AuthenticationPrincipal User user,
                                @RequestParam("productname") String productname,
                                @RequestParam("description") String description,
                                @RequestParam("count") int count,
                                @RequestParam("price") int price,
                                @RequestParam("file") MultipartFile file, Model model) throws IOException {
        if (productname != null && description != null && count > 0 && price > 0) {
            Product product = productRepository.findById(id).orElseThrow();
            product.setProducttitle(productname);
            product.setDescription(description);
            product.setCount(count);
            product.setPrice(price);
            product.setRaiting(0);
            product.setSeller(user);
            if(file != null && !file.getOriginalFilename().isEmpty()) {
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }
                String uuidFile = UUID.randomUUID().toString();
                String resultFilename = uuidFile + "." + file.getOriginalFilename();

                file.transferTo(new File(uploadPath + "/" + resultFilename));
                product.setFilename(resultFilename);
            }
            productRepository.save(product);

        }


        return "redirect:/users/profile";
    }


    @PostMapping("/profile/{id}/remove")
    public String productDelete(@PathVariable(value = "id") Long id, Model model, User user) {
        Product product = productRepository.findById(id).orElseThrow();
        productRepository.delete(product);

        return "redirect:/users/profile";
    }


    @GetMapping("/profile/products/add")
    public String addproduct(@AuthenticationPrincipal User user, Model model) {

        return "products/add";
    }

    @PostMapping("/profile/products/add")
    public String add(@AuthenticationPrincipal User user,
                      @RequestParam("productname") String productname,
                      @RequestParam("description") String description,
                      @RequestParam("count") int count,
                      @RequestParam("price") int price,
                      @RequestParam("category") String category,
                      @RequestParam("file") MultipartFile file) throws IOException {
        if (productname != null && description != null && count > 0 && price > 0) {
            Product product = new Product();
            product.setProducttitle(productname);
            product.setDescription(description);
            product.setCount(count);
            product.setPrice(price);
            product.setCategory(category);
            product.setRaiting(0);
            product.setSeller(user);
            if(file != null && !file.getOriginalFilename().isEmpty()) {
                File uploadDir = new File(uploadPath);
                if (!uploadDir.exists()) {
                    uploadDir.mkdir();
                }
                String uuidFile = UUID.randomUUID().toString();
                String resultFilename = uuidFile + "." + file.getOriginalFilename();

                file.transferTo(new File(uploadPath + "/" + resultFilename));
                product.setFilename(resultFilename);
            }
            productRepository.save(product);

        }
        return "redirect:/home";
    }

}
