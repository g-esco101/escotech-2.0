package com.escotech.repository;

import com.escotech.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByInventoryCountGreaterThan(Integer inventoryCount);
    Product findFirstByIdAndInventoryCountGreaterThan(Long id, Integer inventoryCount);
}
