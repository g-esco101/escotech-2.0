package com.escotech.repository;

import com.github.database.rider.core.api.connection.ConnectionHolder;
import com.github.database.rider.junit5.DBUnitExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;

@ExtendWith({DBUnitExtension.class, SpringExtension.class})
@SpringBootTest
@ActiveProfiles("test")
public class OrderRepositoryTest {

    // products.yml is a dataset that contains the following: 6 images; 3 products; 1 order_line; and 1 order.
    // Products Table contains the following products: 0 id, 1 image, 0 inventory; 1 id, 2 images, 5 inventory; 2 id, 3 images; 10 inventory.
    @Autowired
    private DataSource dataSource;

    @Autowired
    private OrderRepository repository;

    public ConnectionHolder getConnectionHolder() {
        // Return a function that retrieves a connection from our data source
        return () -> dataSource.getConnection();
    }

//    @Test
//    @DataSet("products.yml")
//    @DisplayName("findAllByOrderByIdDesc")
//    void findAllByOrderByIdDesc() {
//        List<Order> orders = repository.findAllByOrderByIdDesc();
//        Assertions.assertEquals(4, orders.size(), "size should be 4");
//        Long id = Long.valueOf("4");
//        for (Order order: orders) {
//            Assertions.assertEquals(id, order.getId(), String.format("id should be %d", id));
//            id--;
//        }
//    }
}
