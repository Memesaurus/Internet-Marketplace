package com.diploma.gazon.repositories;

import com.diploma.gazon.models.Member;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository <M extends Member> extends MongoRepository<M, String> {

}
