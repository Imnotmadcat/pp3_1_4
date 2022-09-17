package ru.kata.spring.boot_security.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;


@Controller
public class MainController {

    private final UserService userService;

    @Autowired
    public MainController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public String user(ModelMap model, Principal principal) {
        User user = userService.findByName(principal.getName());
        model.addAttribute("user", user);
        return "user/user";
    }

    @GetMapping("/admin")
    public String index(Model model, Principal principal) {
        User user = userService.findByName(principal.getName());
        User userNew = new User();
        model.addAttribute("users", userService.index());
        model.addAttribute("user", user);
        model.addAttribute("userNew", userNew);
        return "admin/index";
    }

    @PostMapping("/admin/save")
    public String create(@ModelAttribute("user") User user,
                         @RequestParam(value = "rolesList") String [] roles,
                         @ModelAttribute("password") String password) {
        userService.save(user, roles, password);
        return "redirect:/admin";
    }

    @PutMapping("/admin/{id}/update")
    public String update(@ModelAttribute("user") User user,
                         @RequestParam(value = "rolesList") String [] roles,
                         @ModelAttribute("password") String password) {

        userService.save(user, roles, password);
        return "redirect:/admin";
    }

    @DeleteMapping("/admin/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}



