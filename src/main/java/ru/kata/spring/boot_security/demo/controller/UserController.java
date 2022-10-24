//package ru.kata.spring.boot_security.demo.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.GetMapping;
//import ru.kata.spring.boot_security.demo.service.UserDetailsServiceImpl;
//
//import java.security.Principal;
//
//@Controller
//public class UserController {
//
//    private final UserDetailsServiceImpl userDetailsService;
//
//    @Autowired
//    public UserController(UserDetailsServiceImpl userDetailsService) {
//        this.userDetailsService = userDetailsService;
//    }
//
//    @GetMapping("/user")
//    public String showUserInformation(ModelMap model, Principal principal) {
//        model.addAttribute("user", userDetailsService.loadUserByUsername(principal.getName()));
//        return "user/user";
//    }
//}
