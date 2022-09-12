package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class UserControllerMy {

    private final UserService userService;

    @Autowired
    public UserControllerMy(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String index(Model model) {
        List<User> userList = userService.getAll();
        model.addAttribute("users", userList);
        return "all_users";
    }
//    @GetMapping("")
//    public String startPage(Model model) {
//    List<User> userList = userService.getAll();
//    model.addAttribute("user",userList);
//    model.addAttribute("user.roles", userList);
//    return "admin";
//    }

    @GetMapping("/new")
    public String newUser(Model model) {
        List<Role> roleList = userService.roleList();
        model.addAttribute("user", new User());
        model.addAttribute("roleList", roleList);
        return "new";
    }

    @PostMapping("/new")
    public String create(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "new";
        }
        userService.addUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") long id) {
        User user = userService.getById(id);
        List<Role> roleList = userService.roleList();
        model.addAttribute("user", user);
        model.addAttribute("roleList", roleList);
        return "edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "edit";
        }
        userService.addUser(user);
        return "redirect:/admin";
    }
//    @GetMapping("/update/{id}")
//    public String update(@PathVariable("id") long id, Model model) {
//    User user = userService.getById(id);
//    List<Role> roleList = userService.roleList();
//    model.addAttribute("user", user);
//    model.addAttribute("listRoles", roleList);
//    return "update";
//    }


    //    @PostMapping("/edit/{id}")
//    public String update(@ModelAttribute("user") User user, @PathVariable("id") long id) {
//        userService.update(user);
//        return "redirect:/admin";
//    }
    @DeleteMapping("/{id}/delete")
    public String delete(@ModelAttribute("user") User user) {
        userService.deleteUser(user.getId());
        return "redirect:/admin";
    }
}