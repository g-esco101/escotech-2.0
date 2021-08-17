package com.escotech.service;

import com.escotech.entity.Order;
import com.escotech.entity.Product;
import com.escotech.repository.OrderRepository;
import com.escotech.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

//@CacheConfig(cacheNames = "orderCache")
@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository repository;

    private final ProductRepository productRepository;

    @Autowired
    public OrderService(OrderRepository repository, ProductRepository productRepository) {
        this.repository = repository;
        this.productRepository = productRepository;
    }

//    @Cacheable
    @Transactional(readOnly = true)
    public Optional<Order> findById(Long id) {
        logger.info("find - id: {}", id);
        return repository.findById(id);
    }

//    @CacheEvict(key = "#id")
    @Transactional
    public void deleteById(Long id) {
        logger.info("deleting - id: {}.", id);
        repository.deleteById(id);
    }

    @Transactional
    public void create(Order order) {
        logger.info("create");
        repository.saveAndFlush(order);
    }

    @Transactional(readOnly = true)
    public List<Order> findAll() {
        logger.info("findAll");
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Order> findAllByOrderByIdDesc() {
        logger.info("findAllByOrderByIdDesc");
        return repository.findAllByOrderByIdDesc();
    }

//    @CachePut(key = "#result.id")
    public Order update(Order order) {
        logger.info("update");
        repository.saveAndFlush(order);
        return order;
    }

    public void updateInventoryCount(Order order) {
        logger.info("updateInventoryCount");
        List<Product> products = order.getOrderLines().stream()
                .map(orderLine -> {
                    Optional<Product> productOpt = productRepository.findById(orderLine.getProductId());
                    Product product = productOpt.get();
                    product.setInventoryCount(product.getInventoryCount() - orderLine.getQuantity());
                    return product;
                })
                .collect(Collectors.toList());
        productRepository.saveAll(products);
    }
}
