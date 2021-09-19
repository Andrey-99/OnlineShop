package com.ibs21.OnlineShop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class StartController {
    
    @GetMapping("/")
    public String start() {
        return "start";
    }

    {

    }
}
