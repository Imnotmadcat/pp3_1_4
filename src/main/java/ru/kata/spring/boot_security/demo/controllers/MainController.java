package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private UserDetailsServiceImpl userDetailsService;

    @GetMapping("/")
    public String MainPage() {
//        Role role = new Role(1L,"ROLE_ADMIN");
//        Role role1 = new Role(2L,"ROLE_USER");
//        Set<Role> roles = new HashSet<>();
//        roles.add(role1);
//        User user = new User("user","user","user","user","user@.mail.ru",roles);
//        userService.addUser(user);
//        roles.add(role);
//        User admin = new User("admin","admin","admin","admin","admin@.mail.ru",roles);
//        userService.addUser(admin);
        return "redirect:/login";
    }
}
