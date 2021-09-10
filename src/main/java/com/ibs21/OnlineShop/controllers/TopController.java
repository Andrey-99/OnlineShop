package com.ibs21.OnlineShop.controllers;

import com.ibs21.OnlineShop.repos.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class TopController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("")
    public String home(Model model) {
        model.addAttribute("products", productRepository.findAll());
        return "home-top";
    }
}
