package com.diploma.gazon.controllers;

import com.diploma.gazon.DTO.AuthDTO;
import com.diploma.gazon.models.AdministratorMember;
import com.diploma.gazon.models.CompanyMember;
import com.diploma.gazon.models.Member;
import com.diploma.gazon.models.User.User;
import com.diploma.gazon.models.User.UserRole;
import com.diploma.gazon.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    private UserService<User> userService;

    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("/login")
    public String authenticate(@RequestBody AuthDTO authDTO) {
        return userService.authenticate(authDTO);
    }

    @PostMapping("/register/member")
    public String addMember(@RequestBody Member member) {
        return userService.addUser(member, UserRole.MEMBER);
    }

    @PostMapping("/register/company")
    public String addCompany(@RequestBody CompanyMember companyMember) {
        return userService.addUser(companyMember, UserRole.COMPANY);
    }

    @PostMapping("/register/admin")
    public String addAdministrator(@RequestBody AdministratorMember administratorMember) {
        return userService.addUser(administratorMember, UserRole.ADMIN);
    }
}
