package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    // @ResponseBody
    public String home() {
        return "index";
    }
    
    @GetMapping("/register")
    public String registerPage() {
        return "register"; // This will look for register.html in templates directory
    }
    
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // This will look for login.html in templates directory
    }
    @GetMapping("/users")
    public String UserPage() {
        return "users"; // This will look for login.html in templates directory
    }
    @GetMapping("/admin")
    public String AdminPage() {
        return "admin"; // This will look for login.html in templates directory
    }
    @GetMapping("/home")
    public String HomePage() {
        return "home"; // This will look for login.html in templates directory
    }
}