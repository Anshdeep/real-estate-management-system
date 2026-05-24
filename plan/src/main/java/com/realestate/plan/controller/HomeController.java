package com.realestate.plan.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/home")
    public String home() {
        return "Welcome to the Real Estate Construction & Property Management Platform! (Accessible without authentication)";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "This is the Dashboard. (Requires authentication)";
    }
}
