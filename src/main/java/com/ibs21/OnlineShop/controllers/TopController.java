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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

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

//    @PostMapping("filter")
//    public String filter(@RequestParam String filter, Model model, Pageable pageable){
//        Page<Product> page;
//        page = productRepository.findByProducttitle(filter, pageable);
//        model.addAttribute("page", page);
//        return "home-top";
//    }

    @PostMapping("search")
    public String filter(@RequestParam String filter, Model model, Pageable pageable){
        String message = "Такого товара нет";
        Page<Product> page;
        if (filter != null && !filter.isEmpty()){
            page = productRepository.findByProducttitle(filter, pageable);
            if(page == null || page.isEmpty()){
                model.addAttribute("message", message);
            }
        }else {
            page = productRepository.findAll(pageable);
        }
            model.addAttribute("page", page);
        return "home-top";
    }

}
