package com.ibs21.OnlineShop.controllers;

import com.ibs21.OnlineShop.domain.Product;
import com.ibs21.OnlineShop.domain.User;
import com.ibs21.OnlineShop.repos.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.UUID;

/*
рпнномомfnjngjfm
 */
@Controller
@RequestMapping("/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;


    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/{id}")
    public String product(@PathVariable("id") Long id, Model model) {
        model.addAttribute("product", productRepository.findById(id));
        return "products/product";
    }

    @GetMapping("/add")
    public String addproduct() {
        return "products/add";
    }

    @PostMapping("/add")
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
