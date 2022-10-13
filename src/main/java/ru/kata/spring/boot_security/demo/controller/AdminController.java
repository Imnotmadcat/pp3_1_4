package ru.kata.spring.boot_security.demo.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.Optional;


@Controller
public class AdminController {

    private final UserService userService;
    private int id;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String showAllUsers(Model model, Principal principal) {
        /* Проблема с редактированием пользователя была в том,
        что я получаю пользователя из БД через Email, доставая его через principal
        но после того, как я делаю новый Email в Edit текущему пользователю,
        то происходит ошибка, так как в principal старый
        email и он не находит в БД этого пользователя.
        Я нашёл такое решение через Optional, но это выглядит как жуткий костыль
        Как сделать более правильно?
        */

//  После смены email. пользователь становится Null но я использую старый id
//  который сохраняю в первый раз
        Optional<User> user = Optional.ofNullable(userService.findByEmail(principal.getName()));
        if (user.isPresent()) {
            id = userService.findByEmail(principal.getName()).getId();
        }
        User user1 = userService.findById(id);
        User userNew = new User();
        model.addAttribute("users", userService.allUsers());
        model.addAttribute("user", user1);
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



