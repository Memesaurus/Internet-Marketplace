package com.diploma.gazon.controllers;

import com.diploma.gazon.DTO.AuthDTO;
import com.diploma.gazon.DTO.NewUserDTO;
import com.diploma.gazon.models.User.User;
import com.diploma.gazon.services.UserServices.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.util.Base64;
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
    public void authenticate(@RequestBody AuthDTO authDTO, HttpServletResponse response) {
        String jwt = userService.authenticate(authDTO);

        Cookie cookie = new Cookie("Authentication", jwt);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60 * 20);
        cookie.setPath("/");

        response.addCookie(cookie);
    }

    @PostMapping("/register")
    public String addUser(@RequestBody NewUserDTO newUserDTO) {
        return userService.addUser(newUserDTO);
    }
}
