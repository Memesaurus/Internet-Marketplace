package com.diploma.gazon.controllers;

import com.diploma.gazon.DTO.AuthDTO;
import com.diploma.gazon.models.CompanyUser;
import com.diploma.gazon.models.Member.Member;
import com.diploma.gazon.models.User;
import com.diploma.gazon.services.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    private MemberService<Member> memberService;

    @GetMapping
    public List<Member> getMembers() {
        return memberService.getAllMembers();
    }

    @PostMapping("/login")
    public String authenticate(@RequestBody AuthDTO authDTO) {
        return memberService.authenticate(authDTO);
    }

    @PostMapping("/register/user")
    public String addUser(@RequestBody User user) {
        return memberService.addMember(user);
    }

    @PostMapping("/register/company")
    public String addCompany(@RequestBody CompanyUser companyUser) {
        return memberService.addMember(companyUser);
    }
}
