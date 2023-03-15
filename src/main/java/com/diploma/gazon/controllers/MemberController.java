package com.diploma.gazon.controllers;

import com.diploma.gazon.models.CompanyUser;
import com.diploma.gazon.models.Member;
import com.diploma.gazon.models.User;
import com.diploma.gazon.services.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class MemberController {
    @Autowired
    private MemberService<Member> memberService;

    @GetMapping
    public List<Member> getMembers() {
        return memberService.getAllMembers();
    }

    @PostMapping("/user")
    public void addUser(@RequestBody User user) {
        memberService.addMember(user);
    }

    @PostMapping("/company")
    public void addCompany(@RequestBody CompanyUser companyUser) {
        memberService.addMember(companyUser);
    }
}
