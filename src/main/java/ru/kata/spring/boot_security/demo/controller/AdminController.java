package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;


@Controller
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String showAllUsers(Model model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        User userNew = new User();
        model.addAttribute("users", userService.allUsers());
        model.addAttribute("user", user);
        model.addAttribute("userNew", userNew);
        return "admin/index";
    }

    @PostMapping("/admin/save")
    public String create(@ModelAttribute("user") User user,
                         @RequestParam(value = "rolesList") String[] roles,
                         @ModelAttribute("password") String password) {
        userService.save(user, roles, password);
        return "redirect:/admin";
    }

    @PutMapping("/admin/{id}/update")
    public String update(@ModelAttribute("user") User user,
                         @RequestParam(value = "rolesList") String[] roles,
                         @ModelAttribute("password") String password) {

        userService.update(user, roles);
        return "redirect:/admin";
    }

    @DeleteMapping("/admin/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}



