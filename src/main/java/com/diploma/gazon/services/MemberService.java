package com.diploma.gazon.services;

import com.diploma.gazon.DTO.AuthDTO;
import com.diploma.gazon.config.jwt.JwtService;
import com.diploma.gazon.exceptions.AlreadyExistsException;
import com.diploma.gazon.exceptions.NotFoundException;
import com.diploma.gazon.models.Member.Member;
import com.diploma.gazon.repositories.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService<T extends Member> {
    @Autowired
    private MemberRepository<T> memberRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    public List<T> getAllMembers() {
        return memberRepository.findAll();
    }

    public String addMember(T member) {
        try {
            member.encodePassword();
            member.setIsEnabled(true);
            memberRepository.save(member);
        } catch (DuplicateKeyException exception) {
            throw new AlreadyExistsException("User already exists");
        }

        return jwtService.generateToken(member);
    }

    public String authenticate(AuthDTO authDTO) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authDTO.username,
                        authDTO.password
                )
        );

        Member authenticatedUser = memberRepository.findByUsername(authDTO.username)
                .orElseThrow(NotFoundException::new);

        return jwtService.generateToken(authenticatedUser);
    }

}
