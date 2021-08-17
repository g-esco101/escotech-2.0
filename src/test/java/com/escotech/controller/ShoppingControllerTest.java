package com.escotech.controller;

import com.escotech.TestUtils;
import com.escotech.entity.Product;
import com.escotech.dto.Cart;
import com.escotech.dto.CartProduct;
import com.escotech.service.MapStructMapper;
import com.escotech.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.setup.SharedHttpSessionConfigurer.sharedHttpSession;

@WebMvcTest(ShoppingController.class)
public class ShoppingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @MockBean
    private ProductService service;

    @MockBean
    private MapStructMapper mapper;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                                                .apply(sharedHttpSession())
                                                .build();
    }

    @Test
    @DisplayName("addOneProductToCart")
    public void addOneProductToCart() throws Exception {
        CartProduct cartProduct1 = TestUtils.cartProduct1(5, 1);
        Product product1 = TestUtils.product1(5);

        List<CartProduct> orderLines = new LinkedList<>();
        orderLines.add(cartProduct1);
        Cart cart = TestUtils.cart(orderLines);

        doReturn(cartProduct1).when(mapper).productToCartProduct(product1);
        doReturn(product1).when(service).findByIdAndInventoryCountGreaterThan(1L, 0);

        this.mockMvc
                .perform(post("/addItem/{id}", 1L))
                .andExpect(status().isOk());

        this.mockMvc
                .perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(view().name("cart"))
                .andExpect(model().attribute("purchaserComplete", false))
                .andExpect(model().attribute("addressComplete", false))
                .andExpect(model().attribute("cart", cart));
    }

    @Test
    @DisplayName("addTwoProductsToCart")
    public void addTwoProductsToCart() throws Exception {
        CartProduct cartProduct = TestUtils.cartProduct3(5, 1);
        Product product = TestUtils.product3(5);
        List<CartProduct> orderLines = new LinkedList<>();
        orderLines.add(cartProduct);
        Cart cart = TestUtils.cart(orderLines);

        doReturn(cartProduct).when(mapper).productToCartProduct(product);
        doReturn(product).when(service).findByIdAndInventoryCountGreaterThan(3L, 0);

        this.mockMvc
                .perform(post("/addItem/{id}", 3L))
                .andExpect(status().isOk());

        CartProduct cartProduct1 = TestUtils.cartProduct1(5, 1);
        Product product1 = TestUtils.product1(5);
        cart.getOrderLines().add(cartProduct1);
        cart.calculateCostsAndQuantity();

        doReturn(cartProduct1).when(mapper).productToCartProduct(product1);
        doReturn(product1).when(service).findByIdAndInventoryCountGreaterThan(1L, 0);

        this.mockMvc
                .perform(post("/addItem/{id}", 1L))
                .andExpect(status().isOk());

        this.mockMvc
                .perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(view().name("cart"))
                .andExpect(model().attribute("purchaserComplete", false))
                .andExpect(model().attribute("addressComplete", false))
                .andExpect(model().attribute("cart", cart));
    }

//    @Test
//    @DisplayName("cartAddThenRemoveProduct")
//    public void cartAddThenRemoveProduct() throws Exception {
//        CartProduct cartProduct = TestUtils.cartProduct3(5, 1);
//        Product product = TestUtils.product3(5);
//        Cart cart = new Cart();
//
//        doReturn(cartProduct).when(mapper).productToCartProduct(product);
//        doReturn(product).when(service).findByIdAndInventoryCountGreaterThan(3L, 0);
//
//        this.mockMvc
//                .perform(post("/addItem/{id}", 3L))
//                .andExpect(status().isOk());
//
//        this.mockMvc
//                .perform(get("/removeItem/{id}", 3L))
//                .andExpect(status().isFound())
//                .andExpect(view().name("redirect:/cart"))
//                .andExpect(model().attribute("cart", cart));
//    }

    @Test
    @DisplayName("notAddProductInventory0")
    public void notAddProductInventory0() throws Exception {
        CartProduct cartProduct2 = TestUtils.cartProduct2(0, 1);
        Product product2 = TestUtils.product2(0);

        List<CartProduct> orderLines = new LinkedList<>();
        Cart cart = new Cart();

        doReturn(cartProduct2).when(mapper).productToCartProduct(product2);
        doReturn(null).when(service).findByIdAndInventoryCountGreaterThan(2L, 0);

        this.mockMvc
                .perform(post("/addItem/{id}", 2L))
                .andExpect(status().isOk());

        this.mockMvc
                .perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(view().name("cart"))
                .andExpect(model().attribute("purchaserComplete", false))
                .andExpect(model().attribute("addressComplete", false))
                .andExpect(model().attribute("cart", cart));
    }
}
