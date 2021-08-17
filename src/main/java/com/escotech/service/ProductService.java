package com.escotech.service;

import com.escotech.repository.ProductRepository;
import com.escotech.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

//@CacheConfig(cacheNames = "productCache")
@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository repository;

    @Autowired
    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    // Methods annotated with @Cacheable are not executed again if a value
    // already exists in the cache for the cache key. If the value does not
    // exist in the cache, then the method is executed and places its value in the cache.
//    @Cacheable
    @Transactional
    public Optional<Product> findById(Long id) {
        logger.info("find - id: {}", id);
        return repository.findById(id);
    }

    public List<Product> findAllByInventoryCountGreaterThan(int inventoryCount) {
        logger.info("findAllByInventoryCountGreaterThan - inventoryCount: {}.", inventoryCount);
        return repository.findAllByInventoryCountGreaterThan(inventoryCount);
    }

    public Product findByIdAndInventoryCountGreaterThan(Long id, Integer inventoryCount) {
        logger.info("findByIdAndInventoryCountGreaterThan - id: {}. inventoryCount: {}.", id, inventoryCount);
        return repository.findFirstByIdAndInventoryCountGreaterThan(id, inventoryCount);
    }
    // The method is always executed and its result is placed in the cache.
    // The key #result is a placeholder provided by Spring and refers to the return value of the method.
//    @CachePut(key = "#result.id")
    public Product update(Product product) {
        logger.info("update - id: {}.", product.getId());
        repository.saveAndFlush(product);
        return product;
    }

//    @CacheEvict(key = "#id")
    @Transactional
    public void deleteById(Long id) {
        logger.info("deleting - id: {}.", id);
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<Product> findAll() {
        logger.info("findAll");
        return repository.findAll();
    }

    public int queryQuanty(Long id) {
        logger.info("queryQuanty - id: {}", id);
        return repository.findById(id).map(Product::getInventoryCount).orElse(-1);
    }

    @Transactional
    public void create(Product product) {
        logger.info("create");
        repository.saveAndFlush(product);
    }
}
