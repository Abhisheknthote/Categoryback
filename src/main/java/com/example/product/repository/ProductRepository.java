package com.example.product.repository;

import com.example.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("SELECT COUNT(p) FROM Product p WHERE p.category.categoryId = :categoryId AND p.status = true")
    long countActiveProductsByCategoryId(int categoryId);
}
