package com.gfgtech.product.repository;

import com.gfgtech.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    void deleteById(Long id);
}
