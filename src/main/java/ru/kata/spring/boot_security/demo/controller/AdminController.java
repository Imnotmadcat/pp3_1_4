package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserDetailsServiceImpl;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.security.Principal;

@Controller
public class AdminController {

    private final UserDetailsServiceImpl userDetailsServiceimpl;
    private final UserService userService;

    @Autowired
    public AdminController(UserService userService, UserDetailsServiceImpl userDetailsServiceimpl) {
        this.userService = userService;
        this.userDetailsServiceimpl = userDetailsServiceimpl;
    }

    @GetMapping("/admin")
    public String showAllUsersInformation(Model model, Principal principal) {
        model.addAttribute("users", userService.findAllUsers());
        model.addAttribute("user", userDetailsServiceimpl.loadUserByUsername(principal.getName()));
        model.addAttribute("userNew", new User() );
        return "admin/index";
    }

    @PostMapping("/admin/save")
    public String createNewUser(@ModelAttribute("user") User user,
                                @RequestParam(value = "rolesList") String[] roles,
                                @ModelAttribute("password") String password) {
        userService.saveUser(user, roles, password);
        return "redirect:/admin";
    }

    @PutMapping("/admin/{id}/update")
    public String updateUser(@ModelAttribute("user") User user,
                             @RequestParam(value = "rolesList") String[] roles,
                             @ModelAttribute("password") String password) {

        userService.updateUser(user, roles);
        return "redirect:/admin";
    }

    @DeleteMapping("/admin/{id}/delete")
    public String deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}



