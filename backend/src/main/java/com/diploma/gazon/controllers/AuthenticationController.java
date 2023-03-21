package com.diploma.gazon.controllers;

import com.diploma.gazon.DTO.AuthDTO;
import com.diploma.gazon.DTO.NewUserDTO;
import com.diploma.gazon.models.User.User;
import com.diploma.gazon.services.UserService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin
public class AuthenticationController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/login")
    public String authenticate(@RequestBody AuthDTO authDTO) {
        return userService.authenticate(authDTO);
    }

    @PostMapping("/register")
    public String addUser(@RequestBody NewUserDTO newUserDTO) {
        return userService.addUser(newUserDTO);
    }
}
