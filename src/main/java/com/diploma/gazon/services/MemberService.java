package com.diploma.gazon.services;

import com.diploma.gazon.exceptions.AlreadyExistsException;
import com.diploma.gazon.models.Member;
import com.diploma.gazon.models.User;
import com.diploma.gazon.repositories.MemberRepository;
import com.mongodb.MongoException;
import com.mongodb.MongoServerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService <T extends Member>  {
    @Autowired
    private MemberRepository<T> memberRepository;

    public List<T> getAllMembers() {
        return memberRepository.findAll();
    }

    public void addMember(T member) {
        try {
            memberRepository.save(member);
        } catch (DuplicateKeyException exception) {
            throw new AlreadyExistsException("User already exists");
        }
    }
}
