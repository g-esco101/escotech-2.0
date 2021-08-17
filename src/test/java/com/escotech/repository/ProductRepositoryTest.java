package com.escotech.repository;

import com.escotech.TestUtils;
import com.escotech.entity.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import java.util.List;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ProductRepository repository;

    @BeforeEach
    void setUp() {
        Product product1 = TestUtils.product1(0);
        Product product2 = TestUtils.product2(5);
        Product product3 = TestUtils.product3(10);

        entityManager.merge(product1);
        entityManager.merge(product2);
        entityManager.merge(product3);
    }
    @Test
    @DisplayName("findAllByInventoryCountGreaterThan0")
    void findAllByInventoryCountGreaterThan0() {
        List<Product> products = repository.findAllByInventoryCountGreaterThan(0);
        Assertions.assertEquals(2, products.size(), "size should be 2");
    }

    @Test
    @DisplayName("findAllByInventoryCountGreaterThan5")
    void findAllByInventoryCountGreaterThan5() {
        List<Product> products = repository.findAllByInventoryCountGreaterThan(5);
        Assertions.assertEquals(1, products.size(), "size should be 1");
    }

    @Test
    @DisplayName("findAllByInventoryCountGreaterThan10")
    void findAllByInventoryCountGreaterThan10() {
        List<Product> products = repository.findAllByInventoryCountGreaterThan(10);
        Assertions.assertEquals(0, products.size(), "size should be 0");
    }

    @Test
    @DisplayName("findByIdAndInventoryCountGreaterThan3L0")
    void findByIdAndInventoryCountGreaterThan3L0() {
        Product products = repository.findFirstByIdAndInventoryCountGreaterThan(3L, 10);
        Assertions.assertNull(products, "should be null");
    }

    @Test
    @DisplayName("findByIdAndInventoryCountGreaterThan2L5")
    void findByIdAndInventoryCountGreaterThan2L5() {
        Product product = repository.findFirstByIdAndInventoryCountGreaterThan(2L, 5);
        Assertions.assertNull(product, "should be null");
    }

//    @Test
//    @DisplayName("findByIdAndInventoryCountGreaterThan2L0")
//    void findByIdAndInventoryCountGreaterThan2L0() {
//        Product product = repository.findFirstByIdAndInventoryCountGreaterThan(2L, 0);
//        Assertions.assertNotNull(product, "should not be null");
//        Assertions.assertEquals((long) product.getId(), 2L, "id should be 2L");
//        Assertions.assertTrue(product.getInventoryCount() > 0, "inventory should be greater");
//    }
}
