package com.ibs21.OnlineShop.controllers;

import com.ibs21.OnlineShop.domain.Product;
import com.ibs21.OnlineShop.repos.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.data.domain.Pageable;

@Controller
@RequestMapping("/home")
public class TopController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("")
    public String home(Model model, @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Product> page;
        page = productRepository.findAll(pageable);
        model.addAttribute("page", page);
//        model.addAttribute("url", "/home");
        return "home-top";
    }
}
