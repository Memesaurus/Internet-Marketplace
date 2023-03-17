package com.diploma.gazon.repositories;

import com.diploma.gazon.models.User.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository<T extends User> extends MongoRepository<T, String> {
    Optional<T> findByUsername(String username);
}
