package com.diploma.gazon.repositories;

import com.diploma.gazon.models.Member.Member;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository <T extends Member> extends MongoRepository<T, String> {
    Optional<T> findByUsername(String username);
}
