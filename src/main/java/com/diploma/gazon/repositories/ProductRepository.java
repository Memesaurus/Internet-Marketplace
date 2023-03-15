package com.diploma.gazon.repositories;

import com.diploma.gazon.models.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
}
